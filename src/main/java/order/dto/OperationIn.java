package order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationIn {

    @JsonProperty("operation")
    private String operation;
    @JsonProperty("unit-cost")
    private Double unitCost;
    @JsonProperty("quantity")
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }


}
