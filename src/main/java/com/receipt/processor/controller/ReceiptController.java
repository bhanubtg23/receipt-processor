package com.receipt.processor.controller;

import com.receipt.processor.model.Receipt;
import com.receipt.processor.model.response.PointsResponse;
import com.receipt.processor.model.response.ReceiptResponse;
import com.receipt.processor.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/process")
    public ResponseEntity<ReceiptResponse> processReceipt(@Valid @RequestBody Receipt receipt) {
        String id = receiptService.processReceipt(receipt);
        return ResponseEntity.ok(new ReceiptResponse(id));
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<PointsResponse> getPoints(@PathVariable String id) {
        int points = receiptService.getPoints(id);
        return ResponseEntity.ok(new PointsResponse(points));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Receipt> getReceipt(@PathVariable String id) {
        Receipt receipt = receiptService.getReceipt(id);
        return ResponseEntity.ok(receipt);
    }
}