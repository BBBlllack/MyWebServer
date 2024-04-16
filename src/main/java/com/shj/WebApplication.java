package com.shj;

import com.shj.annotations.Router;
import com.shj.config.WebMapping;
import com.shj.domain.HttpRequest;
import com.shj.domain.HttpResponse;
import com.shj.routers.IndexRouter;
import com.shj.utils.ResourceUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class WebApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebApplication.class);

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        ResourceBundle webProperties = ResourceBundle.getBundle("web");
        String ResourcePath = ResourceUtil.getResourcePath();
        Pattern pattern = Pattern.compile("/[a-zA-Z0-9_/]+\\.[a-zA-Z0-9_]+");
        IndexRouter indexRouter = IndexRouter.class.newInstance();
        int port = 8080;
        try {
            port = Integer.parseInt(webProperties.getString("web.port"));
        } catch (NumberFormatException | MissingResourceException e) {
            System.out.println("Port Exception, use default");
        }
        try {
            System.out.println("Server success to run in port: " + port);
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();
                        HttpRequest httpRequest = new HttpRequest(inputStream);
                        HttpResponse httpResponse = new HttpResponse(outputStream);
                        String uri = httpRequest.getRequestUri();
                        if ("/".equals(uri) || uri == null) {
                            uri = "/index.html";
                        }
                        System.out.println("请求路径: " + uri);
                        if (pattern.matcher(uri).matches()) {
                            // 如果是 /xx.xx 文件格式直接响应文件
                            httpResponse.sendResponse(uri);
                        } else {
                            // 走路由
                            boolean flag = true;
                            Method[] methods = IndexRouter.class.getMethods();
                            for (Method method : methods) {
                                if (method.getAnnotation(Router.class) != null) {
                                    // 有router注解
                                    if (method.getAnnotation(Router.class).value().equals(uri)) {
                                        // 路由匹配成功
                                        flag = false;
                                        method.invoke(indexRouter,
                                                httpRequest,
                                                httpResponse);
                                        break;
                                    }
                                }
                            }
                            // 如果路由匹配失败则响应404
                            if (flag){
                                HttpResponse.SendNotFound(outputStream);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException | StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}