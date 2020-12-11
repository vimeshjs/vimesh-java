package org.vimesh.storage;

import java.util.Date;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageStat {

    private final String name;
    private final long size;
    private final Date last;
    private final Map<String, String> meta;
}
