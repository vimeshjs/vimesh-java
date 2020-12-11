package org.vimesh.storage.options;

import java.util.Map;

import org.springframework.lang.Nullable;
import org.vimesh.storage.Storage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MinioObjectOptions implements Storage.ObjectOptions {

    private Map<String, String> userMetadata;
    
    @Nullable
    public static MinioObjectOptions cast(Storage.ObjectOptions options) {
        return (options instanceof MinioObjectOptions) ? (MinioObjectOptions)options : null;
    }
}
