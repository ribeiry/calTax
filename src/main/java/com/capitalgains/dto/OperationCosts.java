package com.capitalgains.dto;

import java.math.BigDecimal;

public class OperationCosts {
    private BigDecimal averageCost;
    private int totalQuantity;
    private BigDecimal cumulativeLoss;

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getCumulativeLoss() {
        return cumulativeLoss;
    }

    public void setCumulativeLoss(BigDecimal cumulativeLoss) {
        this.cumulativeLoss = cumulativeLoss;
    }

    public OperationCosts(BigDecimal averageCost, int totalQuantity, BigDecimal cumulativeLoss) {
        this.averageCost = averageCost;
        this.totalQuantity = totalQuantity;
        this.cumulativeLoss = cumulativeLoss;
    }
}
