package com.shj.routers;


import com.shj.annotations.Router;
import com.shj.domain.HttpRequest;
import com.shj.domain.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

public class IndexRouter {

    @Router("/i1")
    public void i1(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        System.out.println(httpRequest.getRequestUri());
        httpResponse.getOutputStream().write(buildHttpSuccess(200, "hello world", "text/html").getBytes());
    }

    @Router("/i2")
    public void i2(HttpRequest httpRequest, OutputStream outputStream) {

    }

    public String buildHttpSuccess(Integer status, String content, String contentType) {
        String httpResponse = "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + content.length() + "\r\n" +
                "Connection: close\r\n\r\n" +
                content;
        return httpResponse;
    }
}
