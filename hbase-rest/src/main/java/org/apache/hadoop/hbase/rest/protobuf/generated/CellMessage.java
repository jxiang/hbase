// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: CellMessage.proto

package org.apache.hadoop.hbase.rest.protobuf.generated;

public final class CellMessage {
  private CellMessage() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface CellOrBuilder extends
      // @@protoc_insertion_point(interface_extends:org.apache.hadoop.hbase.rest.protobuf.generated.Cell)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * unused if Cell is in a CellSet
     * </pre>
     *
     * <code>optional bytes row = 1;</code>
     */
    boolean hasRow();
    /**
     * <pre>
     * unused if Cell is in a CellSet
     * </pre>
     *
     * <code>optional bytes row = 1;</code>
     */
    com.google.protobuf.ByteString getRow();

    /**
     * <code>optional bytes column = 2;</code>
     */
    boolean hasColumn();
    /**
     * <code>optional bytes column = 2;</code>
     */
    com.google.protobuf.ByteString getColumn();

    /**
     * <code>optional int64 timestamp = 3;</code>
     */
    boolean hasTimestamp();
    /**
     * <code>optional int64 timestamp = 3;</code>
     */
    long getTimestamp();

    /**
     * <code>optional bytes data = 4;</code>
     */
    boolean hasData();
    /**
     * <code>optional bytes data = 4;</code>
     */
    com.google.protobuf.ByteString getData();
  }
  /**
   * Protobuf type {@code org.apache.hadoop.hbase.rest.protobuf.generated.Cell}
   */
  public  static final class Cell extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:org.apache.hadoop.hbase.rest.protobuf.generated.Cell)
      CellOrBuilder {
    // Use Cell.newBuilder() to construct.
    private Cell(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Cell() {
      row_ = com.google.protobuf.ByteString.EMPTY;
      column_ = com.google.protobuf.ByteString.EMPTY;
      timestamp_ = 0L;
      data_ = com.google.protobuf.ByteString.EMPTY;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Cell(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              row_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              column_ = input.readBytes();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              timestamp_ = input.readInt64();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              data_ = input.readBytes();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell.class, org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell.Builder.class);
    }

    private int bitField0_;
    public static final int ROW_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString row_;
    /**
     * <pre>
     * unused if Cell is in a CellSet
     * </pre>
     *
     * <code>optional bytes row = 1;</code>
     */
    public boolean hasRow() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <pre>
     * unused if Cell is in a CellSet
     * </pre>
     *
     * <code>optional bytes row = 1;</code>
     */
    public com.google.protobuf.ByteString getRow() {
      return row_;
    }

    public static final int COLUMN_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString column_;
    /**
     * <code>optional bytes column = 2;</code>
     */
    public boolean hasColumn() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional bytes column = 2;</code>
     */
    public com.google.protobuf.ByteString getColumn() {
      return column_;
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 3;
    private long timestamp_;
    /**
     * <code>optional int64 timestamp = 3;</code>
     */
    public boolean hasTimestamp() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional int64 timestamp = 3;</code>
     */
    public long getTimestamp() {
      return timestamp_;
    }

    public static final int DATA_FIELD_NUMBER = 4;
    private com.google.protobuf.ByteString data_;
    /**
     * <code>optional bytes data = 4;</code>
     */
    public boolean hasData() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional bytes data = 4;</code>
     */
    public com.google.protobuf.ByteString getData() {
      return data_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, row_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, column_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt64(3, timestamp_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, data_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, row_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, column_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, timestamp_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, data_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell)) {
        return super.equals(obj);
      }
      org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell other = (org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell) obj;

      boolean result = true;
      result = result && (hasRow() == other.hasRow());
      if (hasRow()) {
        result = result && getRow()
            .equals(other.getRow());
      }
      result = result && (hasColumn() == other.hasColumn());
      if (hasColumn()) {
        result = result && getColumn()
            .equals(other.getColumn());
      }
      result = result && (hasTimestamp() == other.hasTimestamp());
      if (hasTimestamp()) {
        result = result && (getTimestamp()
            == other.getTimestamp());
      }
      result = result && (hasData() == other.hasData());
      if (hasData()) {
        result = result && getData()
            .equals(other.getData());
      }
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      if (hasRow()) {
        hash = (37 * hash) + ROW_FIELD_NUMBER;
        hash = (53 * hash) + getRow().hashCode();
      }
      if (hasColumn()) {
        hash = (37 * hash) + COLUMN_FIELD_NUMBER;
        hash = (53 * hash) + getColumn().hashCode();
      }
      if (hasTimestamp()) {
        hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
            getTimestamp());
      }
      if (hasData()) {
        hash = (37 * hash) + DATA_FIELD_NUMBER;
        hash = (53 * hash) + getData().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code org.apache.hadoop.hbase.rest.protobuf.generated.Cell}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:org.apache.hadoop.hbase.rest.protobuf.generated.Cell)
        org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.CellOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell.class, org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell.Builder.class);
      }

      // Construct using org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        row_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        column_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        timestamp_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000004);
        data_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_descriptor;
      }

      public org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell getDefaultInstanceForType() {
        return org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell.getDefaultInstance();
      }

      public org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell build() {
        org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell buildPartial() {
        org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell result = new org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.row_ = row_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.column_ = column_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.timestamp_ = timestamp_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.data_ = data_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell) {
          return mergeFrom((org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell other) {
        if (other == org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell.getDefaultInstance()) return this;
        if (other.hasRow()) {
          setRow(other.getRow());
        }
        if (other.hasColumn()) {
          setColumn(other.getColumn());
        }
        if (other.hasTimestamp()) {
          setTimestamp(other.getTimestamp());
        }
        if (other.hasData()) {
          setData(other.getData());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.ByteString row_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <pre>
       * unused if Cell is in a CellSet
       * </pre>
       *
       * <code>optional bytes row = 1;</code>
       */
      public boolean hasRow() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <pre>
       * unused if Cell is in a CellSet
       * </pre>
       *
       * <code>optional bytes row = 1;</code>
       */
      public com.google.protobuf.ByteString getRow() {
        return row_;
      }
      /**
       * <pre>
       * unused if Cell is in a CellSet
       * </pre>
       *
       * <code>optional bytes row = 1;</code>
       */
      public Builder setRow(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        row_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * unused if Cell is in a CellSet
       * </pre>
       *
       * <code>optional bytes row = 1;</code>
       */
      public Builder clearRow() {
        bitField0_ = (bitField0_ & ~0x00000001);
        row_ = getDefaultInstance().getRow();
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString column_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes column = 2;</code>
       */
      public boolean hasColumn() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional bytes column = 2;</code>
       */
      public com.google.protobuf.ByteString getColumn() {
        return column_;
      }
      /**
       * <code>optional bytes column = 2;</code>
       */
      public Builder setColumn(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        column_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes column = 2;</code>
       */
      public Builder clearColumn() {
        bitField0_ = (bitField0_ & ~0x00000002);
        column_ = getDefaultInstance().getColumn();
        onChanged();
        return this;
      }

      private long timestamp_ ;
      /**
       * <code>optional int64 timestamp = 3;</code>
       */
      public boolean hasTimestamp() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional int64 timestamp = 3;</code>
       */
      public long getTimestamp() {
        return timestamp_;
      }
      /**
       * <code>optional int64 timestamp = 3;</code>
       */
      public Builder setTimestamp(long value) {
        bitField0_ |= 0x00000004;
        timestamp_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int64 timestamp = 3;</code>
       */
      public Builder clearTimestamp() {
        bitField0_ = (bitField0_ & ~0x00000004);
        timestamp_ = 0L;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString data_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes data = 4;</code>
       */
      public boolean hasData() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional bytes data = 4;</code>
       */
      public com.google.protobuf.ByteString getData() {
        return data_;
      }
      /**
       * <code>optional bytes data = 4;</code>
       */
      public Builder setData(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        data_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes data = 4;</code>
       */
      public Builder clearData() {
        bitField0_ = (bitField0_ & ~0x00000008);
        data_ = getDefaultInstance().getData();
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:org.apache.hadoop.hbase.rest.protobuf.generated.Cell)
    }

    // @@protoc_insertion_point(class_scope:org.apache.hadoop.hbase.rest.protobuf.generated.Cell)
    private static final org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell();
    }

    public static org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<Cell>
        PARSER = new com.google.protobuf.AbstractParser<Cell>() {
      public Cell parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new Cell(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Cell> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Cell> getParserForType() {
      return PARSER;
    }

    public org.apache.hadoop.hbase.rest.protobuf.generated.CellMessage.Cell getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021CellMessage.proto\022/org.apache.hadoop.h" +
      "base.rest.protobuf.generated\"D\n\004Cell\022\013\n\003" +
      "row\030\001 \001(\014\022\016\n\006column\030\002 \001(\014\022\021\n\ttimestamp\030\003" +
      " \001(\003\022\014\n\004data\030\004 \001(\014"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_org_apache_hadoop_hbase_rest_protobuf_generated_Cell_descriptor,
        new java.lang.String[] { "Row", "Column", "Timestamp", "Data", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
