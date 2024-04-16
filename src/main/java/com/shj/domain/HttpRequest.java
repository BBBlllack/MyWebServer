package com.shj.domain;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

@Data
public class HttpRequest {

    private InputStream inputStream;

    private String requestUri;

    public HttpRequest(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        parseRequestInputStream(inputStream);
    }

    private void parseRequestInputStream(InputStream inputStream) throws IOException {
        this.requestUri = getRequestUri(inputStream);
    }

    private String getRequestUri(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        String httpInfo = new String(bytes);
        String s = httpInfo.split("\n")[0]; // GET /index.html HTTP/1.1
//        System.out.println(s);
        int index1 = s.indexOf(' '), index2 = s.lastIndexOf(' ');
        if (index1 == -1 || index2 == -1) {
            return null;
        }
        return s.substring(index1 + 1, index2);
    }
}
