package com.receipt.processor.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.receipt.processor.exception.ReceiptNotFoundException;
import com.receipt.processor.model.Receipt;
import com.receipt.processor.util.PointsCalculator;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ReceiptService {

    @Data
    private static class ReceiptInfo {
        private final Receipt receipt;
        private final Integer points;
        private final String id;
    }

    private final PointsCalculator pointsCalculator;
    private final Cache<String, ReceiptInfo> cache;

    public ReceiptService(PointsCalculator pointsCalculator) {
        this.pointsCalculator = pointsCalculator;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .maximumSize(100)
                .recordStats() // Optional: for monitoring cache performance
                .build();
    }

    public String processReceipt(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        int points = pointsCalculator.calculatePoints(receipt);
        ReceiptInfo receiptInfo = new ReceiptInfo(receipt, points, id);
        cache.put(id, receiptInfo);
        return id;
    }

    public int getPoints(String id) {
        ReceiptInfo receiptInfo = cache.getIfPresent(id);
        if (receiptInfo == null) {
            throw new ReceiptNotFoundException("No receipt found for that ID.");
        }
        return receiptInfo.getPoints();
    }

    public Receipt getReceipt(String id) {
        ReceiptInfo receiptInfo = cache.getIfPresent(id);
        if (receiptInfo == null) {
            throw new ReceiptNotFoundException("Receipt not found with id: " + id);
        }
        return receiptInfo.getReceipt();
    }


}