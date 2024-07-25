package com.spring.security.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.security.service.S3UploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3UploadServiceImpl implements S3UploadService {

    @Autowired
    private final AmazonS3 s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.endpoint}")
    private String endPoint;

    String fileName;

    @Override
    public String uploadImage(final MultipartFile multiplePartToFile, final String folderName) {
        try {
            final File file = convertMultiPartFileToFile(multiplePartToFile);
            System.out.println("Uploading to bucket: " + bucketName);
            System.out.println("Region: " + region);
            System.out.println("File name: " + file.getName());

            if (!doesBucketExist(bucketName)) {
                throw new RuntimeException("Bucket " + bucketName + " does not exist.");
            }

            fileName = uploadFileToS3Bucket(bucketName, folderName, file);
            System.out.println("Uploaded file name: " + fileName);
            file.delete();

        } catch (final AmazonServiceException e) {
            System.out.println("Amazon Service Error: " + e.getErrorMessage());
        } catch (final Exception e) {
            System.out.println("General Error: " + e.getMessage());
        }
        return fileName;
    }

    @Override
    public String deleteImage(final String fileName) {
        try {
            s3Client.deleteObject(bucketName, fileName);
            return fileName;
        } catch (final AmazonServiceException e) {
            System.out.println("Error Message: " + e.getErrorMessage());
            return null;
        }
    }

    private File convertMultiPartFileToFile(final MultipartFile multiplePartToFile) {
        File file = new File(multiplePartToFile.getOriginalFilename());
        try (final FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multiplePartToFile.getBytes());
        } catch (final IOException e) {
            System.out.println("Error Message: " + e.getMessage());
        }
        return file;
    }

    private String uploadFileToS3Bucket(final String bucketName, final String folder, final File file) {
        fileName = folder + "/" + System.currentTimeMillis() + "_" + file.getName().replaceAll("\\s+", "_");
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
        s3Client.putObject(putObjectRequest);
        return fileName;
    }

    private boolean doesBucketExist(String bucketName) {
        return s3Client.doesBucketExistV2(bucketName);
    }
}
