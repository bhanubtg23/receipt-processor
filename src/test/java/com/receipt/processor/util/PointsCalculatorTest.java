package com.receipt.processor.util;

import com.receipt.processor.model.Item;
import com.receipt.processor.model.Receipt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointsCalculatorTest {

    private PointsCalculator calculator;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        calculator = new PointsCalculator();
        // Initialize receipt with all required fields that give 0 points
        receipt = new Receipt();
        receipt.setRetailer("");              // 0 points
        receipt.setPurchaseDate("2022-01-02"); // even day = 0 points
        receipt.setPurchaseTime("13:01");      // not between 2-4 PM = 0 points
        receipt.setTotal("0.01");              // not round, not quarter multiple = 0 points
        receipt.setItems(Collections.emptyList());
    }

    @Test
    void shouldCalculateRetailerNamePoints() {
        // Test only retailer name points
        receipt.setRetailer("Target");  // 6 alphanumeric characters
        assertEquals(6, calculator.calculatePoints(receipt));
    }

    @Test
    void shouldCalculateRoundDollarPoints() {
        // Test only round dollar points
        receipt.setTotal("100.00");  // round dollar amount = 50 points + 25 for quarter multiple
        assertEquals(75, calculator.calculatePoints(receipt));
    }

    @Test
    void shouldCalculateQuarterMultiplePoints() {
        // Test only quarter multiple points
        receipt.setTotal("11.75");  // multiple of 0.25 but not round dollar
        assertEquals(25, calculator.calculatePoints(receipt));
    }

    @Test
    void shouldCalculateItemCountPoints() {
        Item item1 = new Item();
        item1.setShortDescription("Test1");  // length not multiple of 3
        item1.setPrice("0.01");

        Item item2 = new Item();
        item2.setShortDescription("Test2");  // length not multiple of 3
        item2.setPrice("0.01");

        receipt.setItems(Arrays.asList(item1, item2));
        // 5 points for having 2 items
        assertEquals(5, calculator.calculatePoints(receipt));
    }

    @Test
    void shouldCalculateItemDescriptionPoints() {
        Item item = new Item();
        item.setShortDescription("ABC");  // length 3 - multiple of 3
        item.setPrice("10.00");          // 10.00 * 0.2 = 2 points

        receipt.setItems(Arrays.asList(item));
        assertEquals(2, calculator.calculatePoints(receipt));
    }

    @Test
    void shouldCalculateOddDayPoints() {
        receipt.setPurchaseDate("2022-01-15");  // odd day
        assertEquals(6, calculator.calculatePoints(receipt));
    }

    @Test
    void shouldCalculateTimeRangePoints() {
        receipt.setPurchaseTime("14:30");  // between 2:00 PM and 4:00 PM
        assertEquals(10, calculator.calculatePoints(receipt));
    }

    @Test
    void shouldCalculateAllPointsTogether() {
        receipt.setRetailer("Target");          // 6 points
        receipt.setPurchaseDate("2022-01-15");  // 6 points (odd day)
        receipt.setPurchaseTime("14:30");       // 10 points (between 2-4 PM)
        receipt.setTotal("100.00");             // 75 points (50 for round + 25 for quarter multiple)

        Item item1 = new Item();
        item1.setShortDescription("ABC");       // length 3 - multiple of 3
        item1.setPrice("10.00");               // 10.00 * 0.2 = 2 points

        Item item2 = new Item();
        item2.setShortDescription("Test");      // length not multiple of 3
        item2.setPrice("0.01");

        receipt.setItems(Arrays.asList(item1, item2));  // 5 points for 2 items

        // Total: 6 + 6 + 10 + 75 + 2 + 5 = 104 points
        assertEquals(104, calculator.calculatePoints(receipt));
    }

    @Test
    void shouldHandleEmptyReceipt() {
        // Using the default setup from setUp() which should give 0 points
        assertEquals(0, calculator.calculatePoints(receipt));
    }
}