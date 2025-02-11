package com.receipt.processor.service;

import com.receipt.processor.exception.ReceiptNotFoundException;
import com.receipt.processor.model.Receipt;
import com.receipt.processor.util.PointsCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReceiptServiceTest {

    @Mock
    private PointsCalculator pointsCalculator;

    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        receiptService = new ReceiptService(pointsCalculator);
    }

    @Test
    void shouldProcessReceiptAndReturnId() {
        Receipt receipt = new Receipt();
        String id = receiptService.processReceipt(receipt);
        assertNotNull(id);
        assertTrue(id.length() > 0);
    }

    @Test
    void shouldReturnPointsForValidId() {
        when(pointsCalculator.calculatePoints(any())).thenReturn(100);

        Receipt receipt = new Receipt();
        String id = receiptService.processReceipt(receipt);

        int points = receiptService.getPoints(id);
        assertEquals(100, points);
    }

    @Test
    void shouldThrowExceptionForInvalidId() {
        assertThrows(ReceiptNotFoundException.class, () ->
                receiptService.getPoints("invalid-id")
        );
    }
}