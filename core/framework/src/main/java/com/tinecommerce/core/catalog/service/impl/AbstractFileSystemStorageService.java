package com.tinecommerce.core.catalog.service.impl;

import java.sql.Timestamp;

public abstract class AbstractFileSystemStorageService
{
    public String getNextFilename() {
        return new Timestamp(System.currentTimeMillis()).getTime() + ".jpg";
    }

}
