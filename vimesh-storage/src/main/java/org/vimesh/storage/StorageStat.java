package org.vimesh.storage;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageStat {

    private final String name;
    private final long size;
    private final Date time;
}
