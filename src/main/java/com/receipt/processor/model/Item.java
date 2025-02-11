package com.receipt.processor.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Item {
    @NotBlank(message = "Short description is required")
    @Pattern(regexp = "^[\\w\\s\\-]+$", message = "Short description must contain only letters, numbers, spaces, and hyphens")
    private String shortDescription;

    @NotBlank(message = "Price is required")
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Price must be in format 0.00")
    private String price;
}