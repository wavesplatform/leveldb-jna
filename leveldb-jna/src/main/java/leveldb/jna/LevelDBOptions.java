package leveldb.jna;

public class LevelDBOptions implements AutoCloseable {
    protected LevelDBNative.Options options;

    private boolean createIfMissing = false;
    private boolean errorIfExists = false;
    private LevelDBCompressionType compressionType = LevelDBCompressionType.SnappyCompression;

    public LevelDBOptions() {
        options = LevelDBNative.leveldb_options_create();
        setCreateIfMissing(createIfMissing);
        setErrorIfExists(errorIfExists);
        setCompressionType(compressionType);
    }

    public void close() {
        if (options != null) {
            LevelDBNative.leveldb_options_destroy(options);
            options = null;
        }
    }

    public boolean isCreateIfMissing() {
        return createIfMissing;
    }

    public void setCreateIfMissing(boolean createIfMissing) {
        if (options != null) {
            this.createIfMissing = createIfMissing;
            LevelDBNative.leveldb_options_set_create_if_missing(options, (byte) (createIfMissing ? 1 : 0));
        }
    }

    public boolean isErrorIfExists() {
        return errorIfExists;
    }

    public void setErrorIfExists(boolean errorIfExists) {
        if (options != null) {
            this.errorIfExists = errorIfExists;
            LevelDBNative.leveldb_options_set_error_if_exists(options, (byte) (errorIfExists ? 1 : 0));
        }
    }

    public LevelDBCompressionType getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(LevelDBCompressionType compressionType) {
        if (options != null) {
            this.compressionType = compressionType;
            LevelDBNative.leveldb_options_set_compression(options, compressionType.getCompressionType());
        }
    }
}
