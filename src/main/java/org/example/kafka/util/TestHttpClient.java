package org.example.kafka.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHttpClient {
    private static final String TARGET_URL = "http://localhost:8080/api/kafka/send";
    private static final int THREAD_POOL_SIZE = 1000;
    private static final int TEST_DURATION_SECONDS = 60;

    private final HttpClient httpClient;
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);

    public TestHttpClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
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
                    .GET()
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

    private void printResults() {
        System.out.println("\nLoad Test Results:");
        System.out.println("Successful requests: " + successCount.get());
        System.out.println("Failed requests: " + errorCount.get());
        System.out.println("Total requests: " + (successCount.get() + errorCount.get()));
    }

    public static void main(String[] args) {
        TestHttpClient loadTester = new TestHttpClient();
        loadTester.startLoadTest();
    }
}

