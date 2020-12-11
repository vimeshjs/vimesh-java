package org.vimesh.storage.options;

import org.springframework.lang.Nullable;
import org.vimesh.storage.Storage;

import com.amazonaws.services.s3.model.CannedAccessControlList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3BucketOptions implements Storage.BucketOptions {

    private CannedAccessControlList cannedAcl;
    
    @Nullable
    public static S3BucketOptions cast(Storage.BucketOptions options) {
        return (options instanceof S3BucketOptions) ? (S3BucketOptions)options : null;
    }
}
