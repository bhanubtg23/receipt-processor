package com.receipt.processor.controller;

import com.receipt.processor.exception.ReceiptNotFoundException;
import com.receipt.processor.model.Receipt;
import com.receipt.processor.service.ReceiptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReceiptController.class)
class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceiptService receiptService;

    @Test
    void shouldProcessReceipt() throws Exception {
        when(receiptService.processReceipt(any(Receipt.class))).thenReturn("test-id");

        String receiptJson = "{"
                + "\"retailer\": \"M&M Corner Market\","
                + "\"purchaseDate\": \"2022-01-01\","
                + "\"purchaseTime\": \"13:01\","
                + "\"items\": ["
                + "  {\"shortDescription\": \"Mountain Dew\", \"price\": \"1.50\"}"
                + "],"
                + "\"total\": \"1.50\""
                + "}";

        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(receiptJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id"));
    }

    @Test
    void shouldGetPoints() throws Exception {
        when(receiptService.getPoints("test-id")).thenReturn(100);

        mockMvc.perform(get("/receipts/test-id/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(100));
    }

    @Test
    void shouldReturn404ForInvalidId() throws Exception {
        when(receiptService.getPoints("invalid-id"))
                .thenThrow(new ReceiptNotFoundException("No receipt found for that ID."));

        mockMvc.perform(get("/receipts/invalid-id/points"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No receipt found for that ID."));
    }
}