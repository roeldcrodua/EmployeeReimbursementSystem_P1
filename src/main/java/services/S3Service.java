package com.revature.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;

public class S3Service {
    private String awsId = "AKIAYI2XA5WMBHMSZ6EQ";
    private String awsKey = "K+hioi4q5e9IwxJQAM64NAxSLCIrLlLcXUoGbxa8";
    private String region = "us-east-2";
    private String bucketName = "testbucketroelcrodua";

    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsId, awsKey);

    AmazonS3 s3Client = AmazonS3ClientBuilder
            .standard()
            .withRegion(Regions.fromName(region))
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .build();

    public void uploadFile(File file) {
        s3Client.putObject(bucketName, "images/" + file.getName(), file);
    }
}
