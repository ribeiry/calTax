package order.services;

import order.dto.OperationIn;
import order.dto.OperationOut;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class CalculatorOperation {

    private static final double TAX_PERCENTAGE = 0.20;
    private static final double OPERATION_THRESHOLD = 20000.00;
    double averageCost = 0.0;
    int totalQuantity = 0;
    double cumulativeLoss = 0.0;
    List<OperationOut> operationOutList = new ArrayList<>();

    public List<OperationOut> calcTax(List<OperationIn> operationInList){
        for (OperationIn op : operationInList) {
            if (op.getOperation().equalsIgnoreCase("buy")) {
                handleBuyOperation(op);
            } else if (op.getOperation().equalsIgnoreCase("sell")) {
                handleSellOperation(op);
            } else {
                handleUnknownOperation();
            }
        }
        return operationOutList;

    }

    private void handleBuyOperation(OperationIn op) {
        // Atualizar média ponderada
        double totalCost = averageCost * totalQuantity + op.getUnitCost() * op.getQuantity();
        totalQuantity += op.getQuantity();
        averageCost = totalCost / totalQuantity;
        averageCost =  round(averageCost);
        // Comprar não gera imposto
        operationOutList.add(new OperationOut(0.00));
    }

    private void handleSellOperation(OperationIn op) {
        double sellTotal = op.getUnitCost() * op.getQuantity();
        double profit = calculateProfit(op);
        totalQuantity -= op.getQuantity();
        double tax = calculateTax(sellTotal, profit);
        operationOutList.add(new OperationOut(tax));
    }

    private double calculateProfit(OperationIn op) {
        double sellPrice = op.getUnitCost();
        double costPrice = averageCost * op.getQuantity();
        return (sellPrice - averageCost) * op.getQuantity();
    }

    private double calculateTax(double sellTotal, double profit) {
        double tax = 0.00;
        if (sellTotal > OPERATION_THRESHOLD) {
            tax = handleTaxableProfit(profit);
        } else if (profit < 0) {
            cumulativeLoss += Math.abs(profit);
        }
        tax = handleCumulativeLoss(profit, sellTotal, tax);
        return tax;
    }

    private double handleTaxableProfit(double profit) {
        double taxableProfit = profit;
        if (profit > 0) {
            if (cumulativeLoss > 0) {
                if (cumulativeLoss >= taxableProfit) {
                    taxableProfit = 0.00;
                    cumulativeLoss -= profit;
                } else {
                    taxableProfit -= cumulativeLoss;
                    cumulativeLoss = 0.00;
                }
            }
            return round(taxableProfit * TAX_PERCENTAGE);
        } else {
            cumulativeLoss += Math.abs(profit);
            return 0.00;
        }
    }

    private double handleCumulativeLoss(double profit, double sellTotal, double tax) {
        if (profit > 0 && cumulativeLoss > 0) {
            if (cumulativeLoss >= profit) {
                tax = 0.00;
            } else {
                double remainingProfit = profit - cumulativeLoss;
                cumulativeLoss = 0.00;
                if (sellTotal > OPERATION_THRESHOLD) {
                    tax = round(remainingProfit * TAX_PERCENTAGE);
                }
            }
        }
        return tax;
    }

    private void handleUnknownOperation() {
        // Operação desconhecida, consideramos imposto 0
        operationOutList.add(new OperationOut(0.00));
    }


}
