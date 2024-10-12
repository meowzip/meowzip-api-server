package com.meowzip.apiserver.aws.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.meowzip.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3UploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String dir;

    @Value("${cloud.aws.cloudfront.url}")
    private String cloudfrontUrl;

    private final AmazonS3 amazonS3;

    /**
     * S3에 파일 업로드
     *
     * @param file   업로드할 파일
     * @param domain 저장할 위치 (도메인)
     * @return Cloudfront url
     */
    public String upload(MultipartFile file, String domain) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String location = dir + File.separator + domain;

        amazonS3.putObject(bucket, location + File.separator + s3FileName, file.getInputStream(), objectMetadata);

        return cloudfrontUrl + File.separator + location + File.separator + s3FileName;
    }

    public String getImagePublicURL(Image image) {
        return cloudfrontUrl + File.separator + dir + File.separator + image.getDomain() + File.separator + image.getName();
    }

    public void delete(String path) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, path));
    }
}
