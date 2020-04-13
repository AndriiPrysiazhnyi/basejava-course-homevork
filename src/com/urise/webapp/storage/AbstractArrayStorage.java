package com.urise.webapp.storage;

import com.urise.webapp.exeption.ExistStorageException;
import com.urise.webapp.exeption.NotExistStorageException;
import com.urise.webapp.exeption.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    private static final int STORAGE_LIMIT = 10000;
    protected static Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage,0,size,null);
        size = 0;
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index <0) {
            throw new NotExistStorageException(r.getUuid());
        }else {
            storage[index]=r;
        }
    }

    /**
     * @return array, contains only Resumes in storage(without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage,0,size);
    }

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index > 0){
            throw new ExistStorageException(r.getUuid());
        }else if(size==STORAGE_LIMIT) {
            throw new StorageException("Storage Overflow", r.getUuid());
        }
        else {
            insertElement(r,index);
            size++;
        }
    }

    protected abstract void insertElement(Resume r, int index);

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            fillDeletedElement(index);
            storage[size-1]=null;
            size--;
        }
    }

    protected abstract void fillDeletedElement(int index);

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return storage[index];
    }

    protected abstract int getIndex(String uuid);


}
