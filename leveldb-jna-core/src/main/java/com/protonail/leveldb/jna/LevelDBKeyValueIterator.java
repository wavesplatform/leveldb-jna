package com.protonail.leveldb.jna;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

public class LevelDBKeyValueIterator extends LevelDBIteratorBase<KeyValuePair> {
    public LevelDBKeyValueIterator(LevelDB levelDB, LevelDBReadOptions readOptions) {
        super(levelDB, readOptions);
        LevelDBNative.leveldb_iter_seek_to_first(iterator);
    }

    public KeyValuePair next() {
        levelDB.checkDatabaseOpen();
        checkIteratorOpen();

        final PointerByReference lengthPtr = new PointerByReference();
        final PointerByReference keyPtr = LevelDBNative.leveldb_iter_key(iterator, lengthPtr);
        final long keyLength = (Native.POINTER_SIZE == 8) ? lengthPtr.getPointer().getLong(0) : lengthPtr.getPointer().getInt(0);
        final byte[] key = keyPtr.getPointer().getByteArray(0, (int) keyLength);
        LevelDBNative.leveldb_free(keyPtr.getPointer());

        final PointerByReference valuePtr = LevelDBNative.leveldb_iter_value(iterator, lengthPtr);
        final long valueLength = (Native.POINTER_SIZE == 8) ? lengthPtr.getPointer().getLong(0) : lengthPtr.getPointer().getInt(0);
        byte[] value = valuePtr.getPointer().getByteArray(0, (int) valueLength);
        LevelDBNative.leveldb_free(valuePtr.getPointer());

        LevelDBNative.leveldb_iter_next(iterator);

        return new KeyValuePair(key, value);
    }
}
