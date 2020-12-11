package org.vimesh.storage.options;

import org.springframework.lang.Nullable;
import org.vimesh.storage.Storage;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.StorageClass;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3ObjectOptions implements Storage.ObjectOptions {

    private ObjectMetadata metadata;
    private CannedAccessControlList cannedAcl;
    private StorageClass storageClass;
    
    @Nullable
    public static S3ObjectOptions cast(Storage.ObjectOptions options) {
        return (options instanceof S3ObjectOptions) ? (S3ObjectOptions)options : null;
    }
}
