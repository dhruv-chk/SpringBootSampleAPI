package org.coi.sampleapp.utils;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class APIUtil {
    String envType;
    String apiUrl = System.getenv("DEMAND_MANAGEMENT_API");

    public APIUtil() {
        this.envType = System.getenv("ENV_TYPE");
    }

    public String postCall(String body, String tenant){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String url = apiUrl + "/resource";
        try {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(body);
            request.addHeader("content-type", "application/json");
            request.addHeader("X-TenantID", tenant);
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String getCall(String zip, String tenant){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String url = apiUrl + zip;
        try {
            HttpGet request = new HttpGet(url);
            request.addHeader("content-type", "application/json");
            request.addHeader("X-TenantID", tenant);
            HttpResponse response = httpClient.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}

