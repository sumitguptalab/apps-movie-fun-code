package org.superbiz.moviefun.dao;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {
    AmazonS3Client s3Client;
    String s3BucketName;
    public S3Store(AmazonS3Client s3Client, String s3BucketName) {
        this.s3Client = s3Client;
        this.s3BucketName = s3BucketName;

        if(!(this.s3Client.doesBucketExist(this.s3BucketName)))
        {
            // Note that CreateBucketRequest does not specify region. So bucket is
            // created in the region specified in the client.
            this.s3Client.createBucket(new CreateBucketRequest(
                    this.s3BucketName));
        }

    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentType(blob.getContentType());
//        objectMetadata.setContentLength(IOUtils.toByteArray(blob.getInputStream()).length);
        s3Client.putObject(this.s3BucketName, blob.getName(), blob.getInputStream(), objectMetadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
       if (!s3Client.doesObjectExist(this.s3BucketName, name)){
           return Optional.empty();
       } else {
           S3Object s3Object = s3Client.getObject(this.s3BucketName, name);
           Blob blob = new Blob(name, s3Object.getObjectContent(), new Tika().detect(name));
           return Optional.of(blob);
       }
    }

    @Override
    public void deleteAll() {

    }

}
