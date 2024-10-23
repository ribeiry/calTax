package com.capitalgains.services;

import com.capitalgains.dto.OperationCosts;
import com.capitalgains.dto.OperationIn;
import com.capitalgains.dto.OperationOut;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SellOperationStrategy implements TaxStrategy{
    private static final BigDecimal TAX_PERCENTAGE = BigDecimal.valueOf(0.20);
    private static final BigDecimal OPERATION_THRESHOLD = BigDecimal.valueOf(20000.00);
    private static final BigDecimal RESET_VARIABLES     = BigDecimal.valueOf(0.00);


    @Override
    public void handleOperation(OperationIn op, List<OperationOut> operationOutList, OperationCosts operationCosts) {
        BigDecimal sellTotal = op.getUnitCost().multiply(BigDecimal.valueOf(op.getQuantity()));
        BigDecimal profit = calculateProfit(op, operationCosts.getAverageCost(), operationCosts.getCumulativeLoss());
        int calcTotalQuantity = operationCosts.getTotalQuantity();
        calcTotalQuantity -= op.getQuantity();
        operationCosts.setTotalQuantity(calcTotalQuantity);
        BigDecimal tax = calculateTax(sellTotal, profit,operationCosts);
        operationOutList.add(new OperationOut(tax));

    }

    private BigDecimal calculateProfit(OperationIn op,BigDecimal averageCost,BigDecimal cumulativeLoss) {
        BigDecimal sellPrice = op.getUnitCost();
        BigDecimal costPrice = averageCost.multiply(BigDecimal.valueOf(op.getQuantity()));
        return sellPrice.subtract(averageCost).multiply(BigDecimal.valueOf(op.getQuantity()));
    }

    private BigDecimal calculateTax(BigDecimal sellTotal, BigDecimal profit, OperationCosts operationCosts) {
        BigDecimal tax = RESET_VARIABLES;
        if (sellTotal.compareTo(OPERATION_THRESHOLD) == 1) {
            tax = handleTaxableProfit(profit,operationCosts);
        } else if (profit.compareTo(BigDecimal.ZERO) == -1) {
            operationCosts.setCumulativeLoss(profit.abs().add(operationCosts.getCumulativeLoss()));
        }
        tax = handleCumulativeLoss(profit, sellTotal, tax, operationCosts);
        return tax;
    }

    private BigDecimal handleTaxableProfit(BigDecimal profit, OperationCosts operationCosts) {
        BigDecimal taxableProfit = profit;
        BigDecimal cumulativeLoss = operationCosts.getCumulativeLoss();
        if (profit.compareTo(BigDecimal.ZERO) == 1) {
            if (cumulativeLoss.compareTo(BigDecimal.ZERO) == 1) {
                if (cumulativeLoss.compareTo(taxableProfit) >= 0) {
                    taxableProfit = RESET_VARIABLES;
                    cumulativeLoss =  cumulativeLoss.subtract(profit);
                    operationCosts.setCumulativeLoss(cumulativeLoss);
                } else {
                    taxableProfit = taxableProfit.subtract(cumulativeLoss);
                    operationCosts.setCumulativeLoss(RESET_VARIABLES);
                }
            }
            return taxableProfit.multiply(TAX_PERCENTAGE).setScale(2,RoundingMode.HALF_UP);
        } else {
            cumulativeLoss =  cumulativeLoss.subtract(profit).abs();
            operationCosts.setCumulativeLoss(cumulativeLoss);
            return RESET_VARIABLES;
        }
    }

    private BigDecimal handleCumulativeLoss(BigDecimal profit, BigDecimal sellTotal, BigDecimal tax, OperationCosts operationCosts) {
        BigDecimal cumulativeLoss = operationCosts.getCumulativeLoss();
        if (profit.compareTo(BigDecimal.ZERO) == 1 && cumulativeLoss.compareTo(BigDecimal.ZERO) == 1  ) {
            if (cumulativeLoss.compareTo( profit) >= 0) {
                tax = RESET_VARIABLES;
            } else {
                BigDecimal remainingProfit = profit.subtract(cumulativeLoss);
                operationCosts.setCumulativeLoss(RESET_VARIABLES);
                if (sellTotal.compareTo(OPERATION_THRESHOLD) == 1) {
                    tax = remainingProfit.multiply(TAX_PERCENTAGE).setScale(2, RoundingMode.HALF_UP);
                }
            }
        }
        return tax;

    }
}
