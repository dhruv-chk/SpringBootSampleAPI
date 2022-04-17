package org.coi.sampleapp.utils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class S3Util {

    String carbon_emission_bucket = System.getenv("SAMPLE_BUCKET");

    public String[] getData(Date current_date) throws IOException {
        try {
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current_date);

            int month = calendar.get(Calendar.MONTH) + 1;
            String key_name = "/sample-file-" + new SimpleDateFormat("yyyyMMdd").format(current_date) + ".json";

            S3Object s3Object = s3.getObject(carbon_emission_bucket, key_name);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            String[] values = IOUtils.toString(s3ObjectInputStream, StandardCharsets.UTF_8).split("\n");

            return values;
        }catch (Exception e) {
            return new String[0];
        }
    }
}
