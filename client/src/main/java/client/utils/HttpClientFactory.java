package client.utils;

import com.google.inject.Provider;

import java.net.http.HttpClient;

public class HttpClientFactory implements Provider<HttpClient> {
    public HttpClient get() {
        return HttpClient.newBuilder().build();
    }
}
