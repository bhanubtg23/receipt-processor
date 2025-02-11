package com.receipt.processor.model.response;

import lombok.Data;

@Data
public class PointsResponse {
    private int points;  // changed from Integer to int to match spec

    public PointsResponse(int points) {
        this.points = points;
    }
}