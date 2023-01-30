package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT = 1337;
    private static final int NUM_THREADS = 20;

    static HttpServer server;

    static {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), NUM_THREADS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ConcurrentHashMap<String, BigDecimal> requestData = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        server.setExecutor(Executors.newFixedThreadPool(NUM_THREADS));
        server.createContext("/", new RequestHandler());
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    public static class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("POST")) {
                InputStream inputStream = exchange.getRequestBody();
                byte[] requestBytes = inputStream.readAllBytes();
                String request = new String(requestBytes);
                String[] parts = request.split(" ");
                if (parts.length > 1) {
                    if (parts[1].equals("end")) {
                        String id = parts[1];
                        BigDecimal sum = BigDecimal.valueOf(Long.parseLong(parts[0]));
                        for (BigDecimal value : requestData.values()) {
                            sum =  sum.add(value);
                        }
                        if (sum.compareTo(BigDecimal.valueOf(10000000000L)) > 0) {
                            String response = "sum is over than 10 billion" + String.valueOf(sum);
                            exchange.sendResponseHeaders(400, response.length());
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write(response.getBytes());
                            outputStream.close();
                            server.stop(1);
                        }
                        String response = String.valueOf(sum) + " " + id;
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(response.getBytes());
                        outputStream.close();
                        requestData.remove(id);
                        server.stop(1);
                    }
                } else {
                    String id = parts[0];
                    BigDecimal num = BigDecimal.valueOf(Long.parseLong(parts[0]));
                    requestData.put(id, requestData.getOrDefault(id, BigDecimal.ZERO).add(num));
                }
            }
        }
    }
}
