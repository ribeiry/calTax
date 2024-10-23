package order.services;

import order.dto.OperationIn;
import order.dto.OperationOut;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;



public class BuyOperationStrategy implements TaxStrategy{
    private BigDecimal averageCost;
    private int totalQuantity;
    private static final BigDecimal RESET_VARIABLES     = BigDecimal.valueOf(0.00);
    public BuyOperationStrategy(BigDecimal averageCost, int totalQuantity) {
        this.averageCost = averageCost;
        this.totalQuantity = totalQuantity;
    }

    @Override
    public void handleOperation(OperationIn op, List<OperationOut> operationOutList) {
        // Atualizar média ponderada
        BigDecimal totalCost =  averageCost.multiply( BigDecimal.valueOf( totalQuantity)).add(op.getUnitCost().multiply( BigDecimal.valueOf(op.getQuantity())));
        totalQuantity += op.getQuantity();
        averageCost = totalCost.divide(BigDecimal.valueOf(totalQuantity));
        averageCost = averageCost.setScale(2, RoundingMode.HALF_UP);
        // Comprar não gera imposto
        operationOutList.add(new OperationOut(RESET_VARIABLES));
    }
}
