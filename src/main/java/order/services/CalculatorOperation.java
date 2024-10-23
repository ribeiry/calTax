package order.services;

import order.dto.OperationIn;
import order.dto.OperationOut;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class CalculatorOperation {

    private static final BigDecimal TAX_PERCENTAGE = BigDecimal.valueOf(0.20);
    private static final BigDecimal OPERATION_THRESHOLD = BigDecimal.valueOf(20000.00);
    private static final BigDecimal RESET_VARIABLES     = BigDecimal.valueOf(0.00);
    private static final String OPERATION_BUY       = "buy";
    private static final String OPERATION_SELL       = "sell";

    BigDecimal averageCost = BigDecimal.valueOf(0.0);
    int totalQuantity = 0;
    BigDecimal cumulativeLoss = BigDecimal.valueOf(0.0);
    List<OperationOut> operationOutList = new ArrayList<>();

    public List<OperationOut> calcTax(List<OperationIn> operationInList){
        for (OperationIn op : operationInList) {
            if (OPERATION_BUY.equalsIgnoreCase(op.getOperation())) {
                handleBuyOperation(op);
            } else if (OPERATION_SELL.equalsIgnoreCase(op.getOperation())) {
                handleSellOperation(op);
            } else {
                handleUnknownOperation();
            }
        }
        return operationOutList;

    }

    private void handleBuyOperation(OperationIn op) {
        // Atualizar média ponderada
        BigDecimal totalCost =  averageCost.multiply( BigDecimal.valueOf( totalQuantity)).add(op.getUnitCost().multiply( BigDecimal.valueOf(op.getQuantity())));
        totalQuantity += op.getQuantity();
        averageCost = totalCost.divide(BigDecimal.valueOf(totalQuantity));
        averageCost = averageCost.setScale(2, RoundingMode.HALF_UP);
        // Comprar não gera imposto
        operationOutList.add(new OperationOut(RESET_VARIABLES));
    }

    private void handleSellOperation(OperationIn op) {
        BigDecimal sellTotal = op.getUnitCost().multiply( BigDecimal.valueOf( op.getQuantity()));
        BigDecimal profit = calculateProfit(op);
        totalQuantity -= op.getQuantity();
        BigDecimal tax = calculateTax(sellTotal, profit);
        operationOutList.add(new OperationOut(tax));
    }

    private BigDecimal calculateProfit(OperationIn op) {
        BigDecimal sellPrice = op.getUnitCost();
        BigDecimal costPrice = averageCost.multiply( BigDecimal.valueOf(op.getQuantity()));
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
        if (profit.compareTo(BigDecimal.valueOf(0)) == 1) {
            if (cumulativeLoss.compareTo(BigDecimal.valueOf(0)) == 1) {
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
        if (profit.compareTo(BigDecimal.valueOf(0)) == 1 && cumulativeLoss.compareTo(BigDecimal.valueOf(0)) == 1  ) {
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

    private void handleUnknownOperation() {
        // Operação desconhecida, consideramos imposto 0
        operationOutList.add(new OperationOut(RESET_VARIABLES));
    }


}
