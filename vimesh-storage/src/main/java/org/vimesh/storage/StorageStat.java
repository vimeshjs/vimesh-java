package org.vimesh.storage;

import java.util.Date;

import lombok.Data;

@Data
public class StorageStat {

    private final String name;
    private final long size;
    private final Date time;
}
