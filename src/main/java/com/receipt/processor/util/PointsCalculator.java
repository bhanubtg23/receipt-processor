package com.receipt.processor.util;

import com.receipt.processor.model.Receipt;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class PointsCalculator {

    public int calculatePoints(Receipt receipt) {
        return calculateRetailerNamePoints(receipt) +
                calculateRoundDollarPoints(receipt) +
                calculateQuarterMultiplePoints(receipt) +
                calculateItemCountPoints(receipt) +
                calculateItemDescriptionPoints(receipt) +
                calculatePurchaseDayPoints(receipt) +
                calculatePurchaseTimePoints(receipt);
    }

    /**
     * Rule: One point for every alphanumeric character in the retailer name
     */
    private int calculateRetailerNamePoints(Receipt receipt) {
        return receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();
    }

    /**
     * Rule: 50 points if the total is a round dollar amount with no cents
     */
    private int calculateRoundDollarPoints(Receipt receipt) {
        double total = Double.parseDouble(receipt.getTotal());
        return (total % 1.0 == 0) ? 50 : 0;
    }

    /**
     * Rule: 25 points if the total is a multiple of 0.25
     */
    private int calculateQuarterMultiplePoints(Receipt receipt) {
        double total = Double.parseDouble(receipt.getTotal());
        return (total % 0.25 == 0) ? 25 : 0;
    }

    /**
     * Rule: 5 points for every two items on the receipt
     */
    private int calculateItemCountPoints(Receipt receipt) {
        return (receipt.getItems().size() / 2) * 5;
    }

    /**
     * Rule: If the trimmed length of the item description is a multiple of 3,
     * multiply the price by 0.2 and round up to the nearest integer
     */
    private int calculateItemDescriptionPoints(Receipt receipt) {
        return receipt.getItems().stream()
                .filter(item -> {
                    String trimmedDescription = item.getShortDescription().trim();
                    return trimmedDescription.length() % 3 == 0;
                })
                .mapToInt(item -> {
                    double price = Double.parseDouble(item.getPrice());
                    return (int) Math.ceil(price * 0.2);
                })
                .sum();
    }

    /**
     * Rule: 6 points if the day in the purchase date is odd
     */
    private int calculatePurchaseDayPoints(Receipt receipt) {
        return receipt.getPurchaseDateAsLocalDate().getDayOfMonth() % 2 != 0 ? 6 : 0;
    }

    /**
     * Rule: 10 points if the time of purchase is after 2:00pm and before 4:00pm
     */
    private int calculatePurchaseTimePoints(Receipt receipt) {
        LocalTime purchaseTime = receipt.getPurchaseTimeAsLocalTime();
        LocalTime start = LocalTime.of(14, 0);
        LocalTime end = LocalTime.of(16, 0);

        return (purchaseTime.isAfter(start) && purchaseTime.isBefore(end)) ? 10 : 0;
    }
}