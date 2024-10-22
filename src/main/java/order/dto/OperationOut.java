package order.dto;

import java.math.BigDecimal;

public class OperationOut {

    public Double tax;

    public OperationOut(Double tax) {
        this.tax = tax;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }
    @Override
    public String toString() {
        return "{" +
                "\"tax\":" + String.format("%.2f",tax) +
                '}';
    }

}
