package order.dto;

import java.math.BigDecimal;

public class OperationOut {

    public BigDecimal tax;

    public OperationOut(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
    @Override
    public String toString() {
        return "{" +
                "\"tax\":" + String.format("%.2f",tax) +
                "}";
    }

}
