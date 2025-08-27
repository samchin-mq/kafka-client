package org.example.kafka.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TestHttpClientString {
    private static final String TARGET_URL = "http://kafka-client3.eba-tsjdhup3.ap-northeast-1.elasticbeanstalk.com/api/kafka/sendString";
    private static final int THREAD_POOL_SIZE = 200;
    private static final int TEST_DURATION_SECONDS = 60;

    private final HttpClient httpClient;
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);
    private final AtomicLong longestElapsed = new AtomicLong(0L);

    public TestHttpClientString() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(60))
                .build();
    }

    public void startLoadTest(Integer num) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Boolean>> futures = new ArrayList<>();
            System.out.println("Starting load test...");

            long startTime = System.currentTimeMillis();
            long endTime = startTime + (TEST_DURATION_SECONDS * 1000);
            for (int i = 0; i < num; i++) {
//            while (System.currentTimeMillis() < endTime) {
                Future<Boolean> future = executor.submit(this::makeRequest);
                futures.add(future);
            }

            // Loop to check if all tasks are done
            boolean allDone = false;
            System.out.println("futures size " + futures.size());
            while (!allDone) {
                allDone = true;
                for (Future<?> future : futures) {
                    if (!future.isDone()) {
                        allDone = false;
                        break;
                    }
                }
                if (!allDone) {
                    try {
                        System.out.println("Not all tasks are done yet. Waiting...");
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            System.out.println("All tasks are done! Retrieving results...");

//            for (Future<Boolean> future : futures) {
//                try {
//                    System.out.println(future.get());
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
            executor.shutdown();
        }

//        shutdownExecutor(executor);
        printResults();
    }


    private boolean makeRequest() {
        Instant start = Instant.now();
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
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        longestElapsed.accumulateAndGet(duration.toMillis(), Math::max);
        return true;
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
        System.out.println("Longest: " + longestElapsed.get());
    }

    public static void main(String[] args) {
        int num = 1000;
        if (args.length > 0) {
            num = Integer.parseInt(args[0]);
        }

        Instant start = Instant.now();
        TestHttpClientString loadTester = new TestHttpClientString();
        loadTester.startLoadTest(num);
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);

        System.out.println("Execution time (milliseconds): " + duration.toMillis());
        System.out.println("Execution time (seconds): " + duration.getSeconds());
        System.out.println("Execution time (nanoseconds): " + duration.toNanos());
    }
}

