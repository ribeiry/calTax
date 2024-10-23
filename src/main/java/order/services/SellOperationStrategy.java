package order.services;

import order.dto.OperationIn;
import order.dto.OperationOut;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SellOperationStrategy implements TaxStrategy{
    private static final BigDecimal TAX_PERCENTAGE = BigDecimal.valueOf(0.20);
    private static final BigDecimal OPERATION_THRESHOLD = BigDecimal.valueOf(20000.00);
    private static final BigDecimal RESET_VARIABLES     = BigDecimal.valueOf(0.00);
    private BigDecimal averageCost;
    private int totalQuantity;
    private BigDecimal cumulativeLoss;

    public SellOperationStrategy(BigDecimal averageCost, int totalQuantity, BigDecimal cumulativeLoss) {
        this.averageCost = averageCost;
        this.totalQuantity = totalQuantity;
        this.cumulativeLoss = cumulativeLoss;
    }

    @Override
    public void handleOperation(OperationIn op, List<OperationOut> operationOutList) {
        BigDecimal sellTotal = op.getUnitCost().multiply(BigDecimal.valueOf(op.getQuantity()));
        BigDecimal profit = calculateProfit(op);
        totalQuantity -= op.getQuantity();
        BigDecimal tax = calculateTax(sellTotal, profit);
        operationOutList.add(new OperationOut(tax));
    }

    private BigDecimal calculateProfit(OperationIn op) {
        BigDecimal sellPrice = op.getUnitCost();
        BigDecimal costPrice = averageCost.multiply(BigDecimal.valueOf(op.getQuantity()));
        return sellPrice.subtract(averageCost).multiply(BigDecimal.valueOf(op.getQuantity()));
    }

    private BigDecimal calculateTax(BigDecimal sellTotal, BigDecimal profit) {
        BigDecimal tax = RESET_VARIABLES;
        if (sellTotal.compareTo(OPERATION_THRESHOLD) == 1) {
            tax = handleTaxableProfit(profit);
        } else if (profit.compareTo(BigDecimal.valueOf(0)) == -1) {
            cumulativeLoss = profit.abs().add(cumulativeLoss);
        }
        tax = handleCumulativeLoss(profit, sellTotal, tax);
        return tax;
    }

    private BigDecimal handleTaxableProfit(BigDecimal profit) {
        BigDecimal taxableProfit = profit;
        if (profit.compareTo(BigDecimal.ZERO) == 1) {
            if (cumulativeLoss.compareTo(BigDecimal.ZERO) == 1) {
                if (cumulativeLoss.compareTo(taxableProfit) >= 0) {
                    taxableProfit = RESET_VARIABLES;
                    cumulativeLoss =  cumulativeLoss.subtract(profit);
                } else {
                    taxableProfit = taxableProfit.subtract(cumulativeLoss);
                    cumulativeLoss = RESET_VARIABLES;
                }
            }
            return taxableProfit.multiply(TAX_PERCENTAGE).setScale(2,RoundingMode.HALF_UP);
        } else {
            cumulativeLoss =  cumulativeLoss.subtract(profit).abs();
            return RESET_VARIABLES;
        }
    }

    private BigDecimal handleCumulativeLoss(BigDecimal profit, BigDecimal sellTotal, BigDecimal tax) {
        if (profit.compareTo(BigDecimal.ZERO) == 1 && cumulativeLoss.compareTo(BigDecimal.ZERO) == 1  ) {
            if (cumulativeLoss.compareTo( profit) >= 0) {
                tax = RESET_VARIABLES;
            } else {
                BigDecimal remainingProfit = profit.subtract(cumulativeLoss);
                cumulativeLoss = RESET_VARIABLES;
                if (sellTotal.compareTo(OPERATION_THRESHOLD) == 1) {
                    tax = remainingProfit.multiply(TAX_PERCENTAGE).setScale(2, RoundingMode.HALF_UP);
                }
            }
        }
        return tax;

    }
}
