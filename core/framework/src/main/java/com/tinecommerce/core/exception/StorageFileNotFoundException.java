package com.tinecommerce.core.exception;

public class StorageFileNotFoundException extends RuntimeException
{
    public StorageFileNotFoundException(String s)
    {
        super(s);
    }

    public StorageFileNotFoundException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public StorageFileNotFoundException(Throwable throwable)
    {
        super(throwable);
    }

    public StorageFileNotFoundException(String s, Throwable throwable, boolean b, boolean b1)
    {
        super(s, throwable, b, b1);
    }
}
