/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License.
 */

package org.apache.hadoop.hbase.quotas;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.classification.InterfaceAudience;
import org.apache.hadoop.hbase.classification.InterfaceStability;
import org.apache.hadoop.hbase.client.ClusterConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.QuotaStatusCalls;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos.GetSpaceQuotaEnforcementsResponse;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos.GetSpaceQuotaEnforcementsResponse.TableViolationPolicy;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos.GetSpaceQuotaRegionSizesResponse;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos.GetSpaceQuotaSnapshotsResponse;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos.GetSpaceQuotaSnapshotsResponse.TableQuotaSnapshot;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos.GetSpaceQuotaRegionSizesResponse.RegionSizes;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos.Quotas;
import org.apache.hadoop.hbase.protobuf.generated.QuotaProtos.SpaceQuota;
import org.apache.hadoop.hbase.ipc.RpcControllerFactory;
import org.apache.hadoop.hbase.util.ByteStringer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Strings;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Helper class to interact with the quota table.
 * 
 * <pre>
 *     ROW-KEY      FAM/QUAL        DATA
 *   n.&lt;namespace&gt; q:s         &lt;global-quotas&gt;
 *   t.&lt;namespace&gt; u:p        &lt;namespace-quota policy&gt;
 *   t.&lt;table&gt;     q:s         &lt;global-quotas&gt;
 *   t.&lt;table&gt;     u:p        &lt;table-quota policy&gt;
 *   u.&lt;user&gt;      q:s         &lt;global-quotas&gt;
 *   u.&lt;user&gt;      q:s.&lt;table&gt; &lt;table-quotas&gt;
 *   u.&lt;user&gt;      q:s.&lt;ns&gt;:   &lt;namespace-quotas&gt;
 * </pre>
 */
@InterfaceAudience.Private
@InterfaceStability.Evolving
public class QuotaTableUtil {
  private static final Log LOG = LogFactory.getLog(QuotaTableUtil.class);

  /** System table for quotas */
  public static final TableName QUOTA_TABLE_NAME = TableName.valueOf(
    NamespaceDescriptor.SYSTEM_NAMESPACE_NAME_STR, "quota");

  protected static final byte[] QUOTA_FAMILY_INFO = Bytes.toBytes("q");
  protected static final byte[] QUOTA_FAMILY_USAGE = Bytes.toBytes("u");
  protected static final byte[] QUOTA_QUALIFIER_SETTINGS = Bytes.toBytes("s");
  protected static final byte[] QUOTA_QUALIFIER_SETTINGS_PREFIX = Bytes.toBytes("s.");
  protected static final byte[] QUOTA_QUALIFIER_POLICY = Bytes.toBytes("p");
  protected static final byte[] QUOTA_USER_ROW_KEY_PREFIX = Bytes.toBytes("u.");
  protected static final byte[] QUOTA_TABLE_ROW_KEY_PREFIX = Bytes.toBytes("t.");
  protected static final byte[] QUOTA_NAMESPACE_ROW_KEY_PREFIX = Bytes.toBytes("n.");

  /*
   * ========================================================================= Quota "settings"
   * helpers
   */
  public static Quotas getTableQuota(final Connection connection, final TableName table)
      throws IOException {
    return getQuotas(connection, getTableRowKey(table));
  }

  public static Quotas getNamespaceQuota(final Connection connection, final String namespace)
      throws IOException {
    return getQuotas(connection, getNamespaceRowKey(namespace));
  }

  public static Quotas getUserQuota(final Connection connection, final String user)
      throws IOException {
    return getQuotas(connection, getUserRowKey(user));
  }

  public static Quotas getUserQuota(final Connection connection, final String user,
      final TableName table) throws IOException {
    return getQuotas(connection, getUserRowKey(user), getSettingsQualifierForUserTable(table));
  }

  public static Quotas getUserQuota(final Connection connection, final String user,
      final String namespace) throws IOException {
    return getQuotas(connection, getUserRowKey(user),
      getSettingsQualifierForUserNamespace(namespace));
  }

  private static Quotas getQuotas(final Connection connection, final byte[] rowKey)
      throws IOException {
    return getQuotas(connection, rowKey, QUOTA_QUALIFIER_SETTINGS);
  }

  private static Quotas getQuotas(final Connection connection, final byte[] rowKey,
      final byte[] qualifier) throws IOException {
    Get get = new Get(rowKey);
    get.addColumn(QUOTA_FAMILY_INFO, qualifier);
    Result result = doGet(connection, get);
    if (result.isEmpty()) {
      return null;
    }
    return quotasFromData(result.getValue(QUOTA_FAMILY_INFO, qualifier));
  }

  public static Get makeGetForTableQuotas(final TableName table) {
    Get get = new Get(getTableRowKey(table));
    get.addFamily(QUOTA_FAMILY_INFO);
    return get;
  }

  public static Get makeGetForNamespaceQuotas(final String namespace) {
    Get get = new Get(getNamespaceRowKey(namespace));
    get.addFamily(QUOTA_FAMILY_INFO);
    return get;
  }

  public static Get makeGetForUserQuotas(final String user, final Iterable<TableName> tables,
      final Iterable<String> namespaces) {
    Get get = new Get(getUserRowKey(user));
    get.addColumn(QUOTA_FAMILY_INFO, QUOTA_QUALIFIER_SETTINGS);
    for (final TableName table : tables) {
      get.addColumn(QUOTA_FAMILY_INFO, getSettingsQualifierForUserTable(table));
    }
    for (final String ns : namespaces) {
      get.addColumn(QUOTA_FAMILY_INFO, getSettingsQualifierForUserNamespace(ns));
    }
    return get;
  }

  public static Scan makeScan(final QuotaFilter filter) {
    Scan scan = new Scan();
    scan.addFamily(QUOTA_FAMILY_INFO);
    if (filter != null && !filter.isNull()) {
      scan.setFilter(makeFilter(filter));
    }
    return scan;
  }

  /**
   * converts quotafilter to serializeable filterlists.
   */
  public static Filter makeFilter(final QuotaFilter filter) {
    FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
    if (!Strings.isEmpty(filter.getUserFilter())) {
      FilterList userFilters = new FilterList(FilterList.Operator.MUST_PASS_ONE);
      boolean hasFilter = false;

      if (!Strings.isEmpty(filter.getNamespaceFilter())) {
        FilterList nsFilters = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        nsFilters.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(
            getUserRowKeyRegex(filter.getUserFilter()), 0)));
        nsFilters.addFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL,
            new RegexStringComparator(getSettingsQualifierRegexForUserNamespace(filter
                .getNamespaceFilter()), 0)));
        userFilters.addFilter(nsFilters);
        hasFilter = true;
      }
      if (!Strings.isEmpty(filter.getTableFilter())) {
        FilterList tableFilters = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        tableFilters.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL,
            new RegexStringComparator(getUserRowKeyRegex(filter.getUserFilter()), 0)));
        tableFilters.addFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL,
            new RegexStringComparator(
                getSettingsQualifierRegexForUserTable(filter.getTableFilter()), 0)));
        userFilters.addFilter(tableFilters);
        hasFilter = true;
      }
      if (!hasFilter) {
        userFilters.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL,
            new RegexStringComparator(getUserRowKeyRegex(filter.getUserFilter()), 0)));
      }

      filterList.addFilter(userFilters);
    } else if (!Strings.isEmpty(filter.getTableFilter())) {
      filterList.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(
          getTableRowKeyRegex(filter.getTableFilter()), 0)));
    } else if (!Strings.isEmpty(filter.getNamespaceFilter())) {
      filterList.addFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(
          getNamespaceRowKeyRegex(filter.getNamespaceFilter()), 0)));
    }
    return filterList;
  }

  /**
   * Creates a {@link Scan} which returns only quota violations from the quota table.
   */
  public static Scan makeQuotaViolationScan() {
    Scan s = new Scan();
    // Limit to "u:v" column
    s.addColumn(QUOTA_FAMILY_USAGE, QUOTA_QUALIFIER_POLICY);
    // Limit rowspace to the "t:" prefix
    s.setRowPrefixFilter(QUOTA_TABLE_ROW_KEY_PREFIX);
    return s;
  }

  /**
   * Extracts the {@link SpaceViolationPolicy} and {@link TableName} from the provided
   * {@link Result} and adds them to the given {@link Map}. If the result does not contain
   * the expected information or the serialized policy in the value is invalid, this method
   * will throw an {@link IllegalArgumentException}.
   *
   * @param result A row from the quota table.
   * @param snapshots A map of violations to add the result of this method into.
   */
  public static void extractQuotaSnapshot(
      Result result, Map<TableName,SpaceQuotaSnapshot> snapshots) {
    byte[] row = Objects.requireNonNull(result).getRow();
    if (null == row) {
      throw new IllegalArgumentException("Provided result had a null row");
    }
    final TableName targetTableName = getTableFromRowKey(row);
    Cell c = result.getColumnLatestCell(QUOTA_FAMILY_USAGE, QUOTA_QUALIFIER_POLICY);
    if (null == c) {
      throw new IllegalArgumentException("Result did not contain the expected column.");
    }
    ByteString buffer = ByteStringer.wrap(
        c.getValueArray(), c.getValueOffset(), c.getValueLength());
    try {
      QuotaProtos.SpaceQuotaSnapshot snapshot = QuotaProtos.SpaceQuotaSnapshot.parseFrom(buffer);
      snapshots.put(targetTableName, SpaceQuotaSnapshot.toSpaceQuotaSnapshot(snapshot));
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(
          "Result did not contain a valid SpaceQuota protocol buffer message", e);
    }
  }

  public static interface UserQuotasVisitor {
    void visitUserQuotas(final String userName, final Quotas quotas) throws IOException;

    void visitUserQuotas(final String userName, final TableName table, final Quotas quotas)
        throws IOException;

    void visitUserQuotas(final String userName, final String namespace, final Quotas quotas)
        throws IOException;
  }

  public static interface TableQuotasVisitor {
    void visitTableQuotas(final TableName tableName, final Quotas quotas) throws IOException;
  }

  public static interface NamespaceQuotasVisitor {
    void visitNamespaceQuotas(final String namespace, final Quotas quotas) throws IOException;
  }

  public static interface QuotasVisitor extends UserQuotasVisitor, TableQuotasVisitor,
      NamespaceQuotasVisitor {
  }

  public static void parseResult(final Result result, final QuotasVisitor visitor)
      throws IOException {
    byte[] row = result.getRow();
    if (isNamespaceRowKey(row)) {
      parseNamespaceResult(result, visitor);
    } else if (isTableRowKey(row)) {
      parseTableResult(result, visitor);
    } else if (isUserRowKey(row)) {
      parseUserResult(result, visitor);
    } else {
      LOG.warn("unexpected row-key: " + Bytes.toString(row));
    }
  }

  public static void
      parseNamespaceResult(final Result result, final NamespaceQuotasVisitor visitor)
          throws IOException {
    String namespace = getNamespaceFromRowKey(result.getRow());
    parseNamespaceResult(namespace, result, visitor);
  }

  protected static void parseNamespaceResult(final String namespace, final Result result,
      final NamespaceQuotasVisitor visitor) throws IOException {
    byte[] data = result.getValue(QUOTA_FAMILY_INFO, QUOTA_QUALIFIER_SETTINGS);
    if (data != null) {
      Quotas quotas = quotasFromData(data);
      visitor.visitNamespaceQuotas(namespace, quotas);
    }
  }

  public static void parseTableResult(final Result result, final TableQuotasVisitor visitor)
      throws IOException {
    TableName table = getTableFromRowKey(result.getRow());
    parseTableResult(table, result, visitor);
  }

  protected static void parseTableResult(final TableName table, final Result result,
      final TableQuotasVisitor visitor) throws IOException {
    byte[] data = result.getValue(QUOTA_FAMILY_INFO, QUOTA_QUALIFIER_SETTINGS);
    if (data != null) {
      Quotas quotas = quotasFromData(data);
      visitor.visitTableQuotas(table, quotas);
    }
  }

  public static void parseUserResult(final Result result, final UserQuotasVisitor visitor)
      throws IOException {
    String userName = getUserFromRowKey(result.getRow());
    parseUserResult(userName, result, visitor);
  }

  protected static void parseUserResult(final String userName, final Result result,
      final UserQuotasVisitor visitor) throws IOException {
    Map<byte[], byte[]> familyMap = result.getFamilyMap(QUOTA_FAMILY_INFO);
    if (familyMap == null || familyMap.isEmpty()) return;

    for (Map.Entry<byte[], byte[]> entry : familyMap.entrySet()) {
      Quotas quotas = quotasFromData(entry.getValue());
      if (Bytes.startsWith(entry.getKey(), QUOTA_QUALIFIER_SETTINGS_PREFIX)) {
        String name = Bytes.toString(entry.getKey(), QUOTA_QUALIFIER_SETTINGS_PREFIX.length);
        if (name.charAt(name.length() - 1) == TableName.NAMESPACE_DELIM) {
          String namespace = name.substring(0, name.length() - 1);
          visitor.visitUserQuotas(userName, namespace, quotas);
        } else {
          TableName table = TableName.valueOf(name);
          visitor.visitUserQuotas(userName, table, quotas);
        }
      } else if (Bytes.equals(entry.getKey(), QUOTA_QUALIFIER_SETTINGS)) {
        visitor.visitUserQuotas(userName, quotas);
      }
    }
  }

  /**
   * Creates a {@link Scan} which only returns violation policy records in the quota table.
   */
  public static Scan getScanForViolations() {
    Scan s = new Scan();
    s.addColumn(QUOTA_FAMILY_USAGE, QUOTA_QUALIFIER_POLICY);
    return s;
  }

  /**
   * Creates a {@link Put} to enable the given <code>policy</code> on the <code>table</code>.
   */
  public static Put putSpaceSnapshot(TableName tableName, SpaceQuotaSnapshot snapshot) {
    Put p = new Put(getTableRowKey(tableName));
    p.addColumn(QUOTA_FAMILY_USAGE, QUOTA_QUALIFIER_POLICY, SpaceQuotaSnapshot.toProtoSnapshot(snapshot).toByteArray());
    return p;
  }


  /* =========================================================================
   *  Space quota status RPC helpers
   */
  /**
   * Fetches the table sizes on the filesystem as tracked by the HBase Master.
   */
  public static Map<TableName,Long> getMasterReportedTableSizes(
      Connection conn) throws IOException {
    if (!(conn instanceof ClusterConnection)) {
      throw new IllegalArgumentException("Expected a ClusterConnection");
    }
    ClusterConnection clusterConn = (ClusterConnection) conn;
    GetSpaceQuotaRegionSizesResponse response = QuotaStatusCalls.getMasterRegionSizes(
        clusterConn, 0);
    Map<TableName,Long> tableSizes = new HashMap<>();
    for (RegionSizes sizes : response.getSizesList()) {
      TableName tn = ProtobufUtil.toTableName(sizes.getTableName());
      tableSizes.put(tn, sizes.getSize());
    }
    return tableSizes;
  }

  /**
   * Fetches the observed {@link SpaceQuotaSnapshot}s observed by a RegionServer.
   */
  public static Map<TableName,SpaceQuotaSnapshot> getRegionServerQuotaSnapshots(
      Connection conn, ServerName regionServer) throws IOException {
    if (!(conn instanceof ClusterConnection)) {
      throw new IllegalArgumentException("Expected a ClusterConnection");
    }
    ClusterConnection clusterConn = (ClusterConnection) conn;
    GetSpaceQuotaSnapshotsResponse response = QuotaStatusCalls.getRegionServerQuotaSnapshot(
        clusterConn, 0, regionServer);
    Map<TableName,SpaceQuotaSnapshot> snapshots = new HashMap<>();
    for (TableQuotaSnapshot snapshot : response.getSnapshotsList()) {
      snapshots.put(
          ProtobufUtil.toTableName(snapshot.getTableName()),
          SpaceQuotaSnapshot.toSpaceQuotaSnapshot(snapshot.getSnapshot()));
    }
    return snapshots;
  }

  /**
   * Fetches the active {@link SpaceViolationPolicy}'s that are being enforced on the
   * given RegionServer.
   */
  public static Map<TableName,SpaceViolationPolicy> getRegionServerQuotaViolations(
      Connection conn, ServerName regionServer) throws IOException {
    if (!(conn instanceof ClusterConnection)) {
      throw new IllegalArgumentException("Expected a ClusterConnection");
    }
    ClusterConnection clusterConn = (ClusterConnection) conn;
    RpcControllerFactory rpcController = clusterConn.getRpcControllerFactory();
    GetSpaceQuotaEnforcementsResponse response =
        QuotaStatusCalls.getRegionServerSpaceQuotaEnforcements(
            clusterConn, rpcController, 0, regionServer);
    Map<TableName,SpaceViolationPolicy> policies = new HashMap<>();
    for (TableViolationPolicy policy : response.getViolationPoliciesList()) {
      policies.put(
          ProtobufUtil.toTableName(policy.getTableName()),
          ProtobufUtil.toViolationPolicy(policy.getViolationPolicy()));
    }
    return policies;
  }

  /* =========================================================================
   *  Quotas protobuf helpers
   */
  protected static Quotas quotasFromData(final byte[] data) throws IOException {
    return quotasFromData(data, 0, data.length);
  }

  protected static Quotas quotasFromData(
      final byte[] data, int offset, int length) throws IOException {
    int magicLen = ProtobufUtil.lengthOfPBMagic();
    if (!ProtobufUtil.isPBMagicPrefix(data, offset, magicLen)) {
      throw new IOException("Missing pb magic prefix");
    }
    return Quotas.parseFrom(new ByteArrayInputStream(data, offset + magicLen, length - magicLen));
  }

  protected static byte[] quotasToData(final Quotas data) throws IOException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    stream.write(ProtobufUtil.PB_MAGIC);
    data.writeTo(stream);
    return stream.toByteArray();
  }

  public static boolean isEmptyQuota(final Quotas quotas) {
    boolean hasSettings = false;
    hasSettings |= quotas.hasThrottle();
    hasSettings |= quotas.hasBypassGlobals();
    // Only when there is a space quota, make sure there's actually both fields provided
    // Otherwise, it's a noop.
    if (quotas.hasSpace()) {
      hasSettings |= (quotas.getSpace().hasSoftLimit() && quotas.getSpace().hasViolationPolicy());
    }
    return !hasSettings;
  }

  /*
   * ========================================================================= HTable helpers
   */
  protected static Result doGet(final Connection connection, final Get get) throws IOException {
    try (Table table = connection.getTable(QUOTA_TABLE_NAME)) {
      return table.get(get);
    }
  }

  protected static Result[] doGet(final Connection connection, final List<Get> gets)
      throws IOException {
    try (Table table = connection.getTable(QUOTA_TABLE_NAME)) {
      return table.get(gets);
    }
  }

  /*
   * ========================================================================= Quota table row key
   * helpers
   */
  protected static byte[] getUserRowKey(final String user) {
    return Bytes.add(QUOTA_USER_ROW_KEY_PREFIX, Bytes.toBytes(user));
  }

  protected static byte[] getTableRowKey(final TableName table) {
    return Bytes.add(QUOTA_TABLE_ROW_KEY_PREFIX, table.getName());
  }

  protected static byte[] getNamespaceRowKey(final String namespace) {
    return Bytes.add(QUOTA_NAMESPACE_ROW_KEY_PREFIX, Bytes.toBytes(namespace));
  }

  protected static byte[] getSettingsQualifierForUserTable(final TableName tableName) {
    return Bytes.add(QUOTA_QUALIFIER_SETTINGS_PREFIX, tableName.getName());
  }

  protected static byte[] getSettingsQualifierForUserNamespace(final String namespace) {
    return Bytes.add(QUOTA_QUALIFIER_SETTINGS_PREFIX,
      Bytes.toBytes(namespace + TableName.NAMESPACE_DELIM));
  }

  protected static String getUserRowKeyRegex(final String user) {
    return getRowKeyRegEx(QUOTA_USER_ROW_KEY_PREFIX, user);
  }

  protected static String getTableRowKeyRegex(final String table) {
    return getRowKeyRegEx(QUOTA_TABLE_ROW_KEY_PREFIX, table);
  }

  protected static String getNamespaceRowKeyRegex(final String namespace) {
    return getRowKeyRegEx(QUOTA_NAMESPACE_ROW_KEY_PREFIX, namespace);
  }

  private static String getRowKeyRegEx(final byte[] prefix, final String regex) {
    return '^' + Pattern.quote(Bytes.toString(prefix)) + regex + '$';
  }

  protected static String getSettingsQualifierRegexForUserTable(final String table) {
    return '^' + Pattern.quote(Bytes.toString(QUOTA_QUALIFIER_SETTINGS_PREFIX)) + table + "(?<!"
        + Pattern.quote(Character.toString(TableName.NAMESPACE_DELIM)) + ")$";
  }

  protected static String getSettingsQualifierRegexForUserNamespace(final String namespace) {
    return '^' + Pattern.quote(Bytes.toString(QUOTA_QUALIFIER_SETTINGS_PREFIX)) + namespace
        + Pattern.quote(Character.toString(TableName.NAMESPACE_DELIM)) + '$';
  }

  protected static boolean isNamespaceRowKey(final byte[] key) {
    return Bytes.startsWith(key, QUOTA_NAMESPACE_ROW_KEY_PREFIX);
  }

  protected static String getNamespaceFromRowKey(final byte[] key) {
    return Bytes.toString(key, QUOTA_NAMESPACE_ROW_KEY_PREFIX.length);
  }

  protected static boolean isTableRowKey(final byte[] key) {
    return Bytes.startsWith(key, QUOTA_TABLE_ROW_KEY_PREFIX);
  }

  protected static TableName getTableFromRowKey(final byte[] key) {
    return TableName.valueOf(Bytes.toString(key, QUOTA_TABLE_ROW_KEY_PREFIX.length));
  }

  protected static boolean isUserRowKey(final byte[] key) {
    return Bytes.startsWith(key, QUOTA_USER_ROW_KEY_PREFIX);
  }

  protected static String getUserFromRowKey(final byte[] key) {
    return Bytes.toString(key, QUOTA_USER_ROW_KEY_PREFIX.length);
  }

  protected static SpaceQuota getProtoViolationPolicy(SpaceViolationPolicy policy) {
    return SpaceQuota.newBuilder()
          .setViolationPolicy(ProtobufUtil.toProtoViolationPolicy(policy))
          .build();
  }

  protected static SpaceViolationPolicy getViolationPolicy(SpaceQuota proto) {
    if (!proto.hasViolationPolicy()) {
      throw new IllegalArgumentException("Protobuf SpaceQuota does not have violation policy.");
    }
    return ProtobufUtil.toViolationPolicy(proto.getViolationPolicy());
  }
}
