package com.carota.vimesh.storage;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class StorageStat {

    private final String name;
    private final long size;
    private final ZonedDateTime time;
}
