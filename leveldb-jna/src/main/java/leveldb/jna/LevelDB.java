package leveldb.jna;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class LevelDB implements AutoCloseable {
    protected LevelDBNative.LevelDB levelDB;

    public LevelDB(String levelDBDirectory, LevelDBOptions options) {
        PointerByReference error = new PointerByReference();
        levelDB = LevelDBNative.leveldb_open(options.options, levelDBDirectory, error);
        LevelDBNative.checkError(error);
    }

    public void close() {
        if (levelDB != null) {
            LevelDBNative.leveldb_close(levelDB);
            levelDB = null;
        }
    }

    public byte[] get(byte[] key, LevelDBReadOptions readOptions) {
        IntByReference resultLength = new IntByReference();

        long keyLength = key != null ? key.length : 0;
        PointerByReference error = new PointerByReference();
        PointerByReference result = LevelDBNative.leveldb_get(levelDB, readOptions.readOptions, key, keyLength, resultLength, error);
        LevelDBNative.checkError(error);

        return result != null ? result.getPointer().getByteArray(0, resultLength.getValue()) : null;
    }

    public void put(byte[] key, byte[] value, LevelDBWriteOptions writeOptions) {
        long keyLength = key != null ? key.length : 0;
        long valueLength = value != null ? value.length : 0;
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_put(levelDB, writeOptions.writeOptions, key, keyLength, value, valueLength, error);
        LevelDBNative.checkError(error);
    }

    public void delete(byte[] key, LevelDBWriteOptions writeOptions) {
        long keyLength = key != null ? key.length : 0;
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_delete(levelDB, writeOptions.writeOptions, key, keyLength, error);
        LevelDBNative.checkError(error);
    }

    public String property(String property) {
        return LevelDBNative.leveldb_property_value(levelDB, property);
    }

    public static void repair(String levelDBDirectory, LevelDBOptions options) {
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_repair_db(options.options, levelDBDirectory, error);
        LevelDBNative.checkError(error);
    }

    public static void destroy(String levelDBDirectory, LevelDBOptions options) {
        PointerByReference error = new PointerByReference();
        LevelDBNative.leveldb_destroy_db(options.options, levelDBDirectory, error);
        LevelDBNative.checkError(error);
    }

    public static int majorVersion() {
        return LevelDBNative.leveldb_major_version();
    }

    public static int minorVersion() {
        return LevelDBNative.leveldb_minor_version();
    }
}
