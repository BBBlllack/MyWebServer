package com.shj.domain;

import com.shj.config.ContentType;
import com.shj.config.HttpStatus;
import com.shj.utils.ResourceUtil;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

@Data
public class HttpResponse {
    public int minimumAddedCoins(int[] coins, int target) {
        int len = coins.length;
        HashSet<Integer> dp = new HashSet<>();
        dp.add(0);

        for (int coin : coins) {
            HashSet<Integer> newDp = new HashSet<>(dp);
            for (int num : dp) {
                int sum = num + coin;
                if (sum <= target) {
                    newDp.add(sum);
                }
            }
            dp = newDp;
        }

        int s = 0;
        while (!dp.contains(target)) {
            HashSet<Integer> newDp = new HashSet<>(dp);
            for (int num : dp) {
                int sum = num + 1;
                if (sum <= target) {
                    newDp.add(sum);
                }
            }
            dp = newDp;
            s++;
        }

        return s;
    }
    private Integer status;
    private OutputStream outputStream;

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendResponse(String uri) throws IOException {
        File file = new File(ResourceUtil.getResourceStaticPath(), uri);
        String ext = null;
        if (uri.contains(".")) {
            ext = uri.substring(uri.lastIndexOf(".") + 1).toLowerCase();
        }
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(new File(ResourceUtil.getResourceStaticPath(), uri));
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();
            String s = new String(bytes);
            if ("html".equals(ext)) {
                SuccessResponse(s, ContentType.HTML);
            }
            if ("png".equals(ext)) {
                SuccessResponse(bytes, ContentType.PNG);
            }
            if ("css".equals(ext)) {
                SuccessResponse(s, ContentType.CSS);
            }
            if ("mp4".equals(ext)) {
                SuccessResponse(bytes, ContentType.MP4);
            }
        } else {
            this.outputStream.write(buildHttpError(HttpStatus.NOT_FOUND, "File Not Found!").getBytes());
        }
    }

    public void SuccessResponse(byte[] content, String contentType) throws IOException {
        this.outputStream.write(buildHttpSuccess(HttpStatus.SUCCESS, content, contentType));
    }

    public void SuccessResponse(String content, String contentType) throws IOException {
        this.outputStream.write(buildHttpSuccess(HttpStatus.SUCCESS, content, contentType).getBytes());
    }

    public String buildHttpError(Integer status, String message) {
        String httpResponse = "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + message.length() + "\r\n" +
                "Connection: close\r\n\r\n" +
                message;
        return httpResponse;
    }

    public String buildHttpSuccess(Integer status, String content, String contentType) {
        String httpResponse = "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + content.length() + "\r\n" +
                "Connection: close\r\n\r\n" +
                content;
        return httpResponse;
    }

    public byte[] buildHttpSuccess(Integer status, byte[] content, String contentType) {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(status).append("\r\n")
                .append("Content-Type: ").append(contentType).append("\r\n")
                .append("Content-Length: ").append(content.length).append("\r\n")
                .append("Connection: close\r\n\r\n");
        byte[] headerBytes = response.toString().getBytes();
        byte[] responseBytes = new byte[headerBytes.length + content.length];
        System.arraycopy(headerBytes, 0, responseBytes, 0, headerBytes.length);
        System.arraycopy(content, 0, responseBytes, headerBytes.length, content.length);
        return responseBytes;
    }

    public static void SendNotFound(OutputStream outputStream) throws IOException {
        String httpResponse = "HTTP/1.1 " + "404" + "\r\n" +
                "Content-Type: " + "text/html" + "\r\n" +
                "Content-Length: " + "Router Not Found".length() + "\r\n" +
                "Connection: close\r\n\r\n" +
                "Router Not Found";
        outputStream.write(httpResponse.getBytes());
    }
}
