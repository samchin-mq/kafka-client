package org.example.kafka.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestHttpClientString3 {
    private static final String TARGET_URL = "http://localhost:8080/api/kafka/sendString3";
    private static final String STAT_URL = "http://localhost:8080/api/kafka/stat";
    private static final int THREAD_POOL_SIZE = 1000;
    private static final int TEST_DURATION_SECONDS = 60*5;

    private final HttpClient httpClient;
    private final AtomicInteger totalCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);
    private final AtomicLong longest = new AtomicLong(0);
    private final AtomicLong fastest = new AtomicLong(10_000);

    public TestHttpClientString3() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public void startLoadTest() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
        
        System.out.println("Starting load test...");

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (TEST_DURATION_SECONDS * 1000);

        while (System.currentTimeMillis() < endTime) {
            executor.submit(this::makeRequest);
        }
            shutdownExecutor(executor);
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            printResults();
            getStat();
        }
        
        
    }
    
    private void getStat() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(STAT_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
        }
    }

    private void makeRequest() {
        Instant start = Instant.now();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TARGET_URL))
                    .GET()
                    .build();
            totalCount.incrementAndGet();
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                successCount.incrementAndGet();
            } else {
                System.out.println("error status code" + response.statusCode());
                errorCount.incrementAndGet();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            errorCount.incrementAndGet();
        }
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        longest.updateAndGet( current -> Math.max(current, duration.toMillis()));
        fastest.updateAndGet( current -> Math.min(current, duration.toMillis()));
        
    }

    private void shutdownExecutor(ExecutorService executor) {
        try {
            executor.shutdown();
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
        System.out.println("Total requests: " + totalCount.get());
        System.out.println("Longest request: " + longest.get());
        System.out.println("Fastest request: " + fastest.get());
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        TestHttpClientString3 loadTester = new TestHttpClientString3();
        loadTester.startLoadTest();
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        
        System.out.println("Execution time (milliseconds): " + duration.toMillis());
        System.out.println("Execution time (seconds): " + duration.getSeconds());
        System.out.println("Execution time (nanoseconds): " + duration.toNanos());
        System.out.println("sam3broker");
    }
}

