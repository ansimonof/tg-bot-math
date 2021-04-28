package org.tgbot.mathunsinn.util.http;

import org.apache.http.Header;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.tgbot.mathunsinn.util.StringUtils;

import java.util.Arrays;

public class HttpUtils {

    public static HttpResponse sendHttpGetRequest(CloseableHttpClient httpClient, String requestString) {
        HttpGet httpGet = new HttpGet(requestString);
        httpGet.setHeader("Accept", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return new HttpResponse(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static HttpResponse sendHttpPostRequest(CloseableHttpClient httpClient, String requestString, String params) {
        HttpPost httpPost = new HttpPost(requestString);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(params, "UTF-8"));
        for (Header allHeader : httpPost.getAllHeaders()) {
            System.out.println(Arrays.toString(allHeader.getElements()));
        }
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String httpResponse = EntityUtils.toString(response.getEntity());
            return new HttpResponse(statusCode, httpResponse);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static CloseableHttpClient getHttpClient(String login, String password) {
        if (!StringUtils.isEmpty(login) && !StringUtils.isEmpty(password)) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(login, password));
            return HttpClients.custom()
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .build();

        }
        return HttpClients.createDefault();
    }

    public static class HttpResponse {
        private int statusCode;
        private String body;

        public HttpResponse(int statusCode, String body) {
            this.body = body;
            this.statusCode = statusCode;
        }

        public String getBody() {
            return body;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }
}
