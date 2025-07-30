package org.example.kafka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.kafka.dto.Order;
import org.example.kafka.dto.OrderShares;
import java.util.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHttpClient2 {
    private static final String TARGET_URL = "http://localhost:8080/api/kafka/sendOrder";
    private static final int THREAD_POOL_SIZE = 1000;
    private static final int TEST_DURATION_SECONDS = 60;

    private final HttpClient httpClient;
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);

    public TestHttpClient2() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(60))
                .build();
    }

    public void startLoadTest() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        System.out.println("Starting load test...");

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (TEST_DURATION_SECONDS * 1000);

        while (System.currentTimeMillis() < endTime) {
            executor.submit(this::makeRequest);
        }

        shutdownExecutor(executor);
        printResults();
    }

    private void makeRequest() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TARGET_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(convertOrdersToJsonString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                successCount.incrementAndGet();
            } else {
                errorCount.incrementAndGet();
            }
        } catch (Exception e) {
            errorCount.incrementAndGet();
        }
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    private String convertOrdersToJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(orders());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    private List<Order> orders() {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            orders.add(generateOrder());
        }
        return orders;
    }

    public Order generateOrder() {
        // Create and populate Order with random data
        Order order = new Order();
        order.setId(String.valueOf(System.currentTimeMillis()));
        order.setLottery("LOTTERY_" + (int)(Math.random() * 10));
        order.setUserid("USER_" + (int)(Math.random() * 1000));
        order.setUsername("Player" + (int)(Math.random() * 100));
        order.setGame("GAME_" + (int)(Math.random() * 5));
        order.setAmount(Math.random() * 1000);
        order.setMultiple((int)(Math.random() * 10) + 1);
        order.setState("PENDING");
        order.setCreated(new Date());
        order.setSaveTime(new Date());
        order.setChannel("WEB");
        order.setCm(Math.random() * 0.1);

        // Create array of OrderShares with random data
        int numShares = (int)(Math.random() * 3) + 1; // 1-3 shares
        OrderShares[] shares = new OrderShares[numShares];

        for (int i = 0; i < numShares; i++) {
            OrderShares orderShare = new OrderShares();
            orderShare.setBid(order.getId() + "_" + i);
            orderShare.setUserid(order.getUserid());
            orderShare.setParentid("PARENT_" + (int)(Math.random() * 100));
            orderShare.setChildid("CHILD_" + (int)(Math.random() * 100));
            orderShare.setShare(Math.random() * 100);
            orderShare.setAmount(Math.random() * order.getAmount());
            orderShare.setShareAmount(orderShare.getAmount() * orderShare.getShare() / 100);
            orderShare.setCm(Math.random() * 0.05);
            orderShare.setScm(Math.random() * 0.03);

            shares[i] = orderShare;
        }

        order.setShares(shares);
        return order;
    }

    private void printResults() {
        System.out.println("\nLoad Test Results:");
        System.out.println("Successful requests: " + successCount.get());
        System.out.println("Failed requests: " + errorCount.get());
        System.out.println("Total requests: " + (successCount.get() + errorCount.get()));
    }

    public static void main(String[] args) {
        TestHttpClient2 loadTester = new TestHttpClient2();
        loadTester.startLoadTest();
    }
}

