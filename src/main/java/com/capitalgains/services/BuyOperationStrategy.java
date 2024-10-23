package com.capitalgains.services;

import com.capitalgains.dto.OperationCosts;
import com.capitalgains.dto.OperationIn;
import com.capitalgains.dto.OperationOut;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;



public class BuyOperationStrategy implements TaxStrategy{
    private static final BigDecimal RESET_VARIABLES     = BigDecimal.valueOf(0.00);


    @Override
    public void handleOperation(OperationIn op, List<OperationOut> operationOutList, OperationCosts operationCosts) {
        // Atualizar média ponderada
        int totalQuantity = operationCosts.getTotalQuantity();
        BigDecimal totalCost =  operationCosts.getAverageCost().multiply(
                BigDecimal.valueOf( totalQuantity)).
                add(op.getUnitCost().
                        multiply( BigDecimal.valueOf(op.getQuantity())));
        totalQuantity += op.getQuantity();
        operationCosts.setTotalQuantity(totalQuantity);
        operationCosts.setAverageCost(totalCost.divide(BigDecimal.valueOf(totalQuantity))
                .setScale(2, RoundingMode.HALF_UP));
        // Comprar não gera imposto
        operationOutList.add(new OperationOut(RESET_VARIABLES));
    }
}
