package com.receipt.processor.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class Receipt {
    @NotBlank(message = "Retailer is required")
    @Pattern(regexp = "^[\\w\\s\\-&]+$", message = "Retailer must contain only letters, numbers, spaces, hyphens, and ampersands")
    private String retailer;

    @NotBlank(message = "Purchase date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Purchase date must be in the format YYYY-MM-DD")
    private String purchaseDate;

    @NotBlank(message = "Purchase time is required")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "Purchase time must be in the format HH:MM")
    private String purchaseTime;

    @NotEmpty(message = "Items cannot be empty")
    @Size(min = 1, message = "At least one item is required")
    @Valid
    private List<Item> items;

    @NotBlank(message = "Total is required")
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Total must be in format 0.00")
    private String total;

    public LocalDate getPurchaseDateAsLocalDate() {
        return LocalDate.parse(purchaseDate);
    }

    public LocalTime getPurchaseTimeAsLocalTime() {
        return LocalTime.parse(purchaseTime);
    }

    public double getTotalAsDouble() {
        return Double.parseDouble(total);
    }
}