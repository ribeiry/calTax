package order.services;

import order.dto.OperationIn;
import order.dto.OperationOut;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

public class CalculatorOperation {

    private static final String OPERATION_BUY = "buy";
    private static final String OPERATION_SELL = "sell";
    private BigDecimal averageCost = BigDecimal.ZERO;
    private int totalQuantity = 0;
    private BigDecimal cumulativeLoss = BigDecimal.ZERO;
    private List<OperationOut> operationOutList = new ArrayList<>();

    private final Map<String, TaxStrategy> strategyMap = new HashMap<>();

    public CalculatorOperation() {
        strategyMap.put(OPERATION_BUY, new BuyOperationStrategy(averageCost, totalQuantity));
        strategyMap.put(OPERATION_SELL, new SellOperationStrategy(averageCost, totalQuantity, cumulativeLoss));
        strategyMap.put("unknown", new UnknowOperationStrategy());
    }

    public List<OperationOut> calcTax(List<OperationIn> operationInList) {
        for (OperationIn op : operationInList) {
            TaxStrategy strategy = strategyMap.getOrDefault(op.getOperation().toLowerCase(), strategyMap.get("unknown"));
            strategy.handleOperation(op, operationOutList);
        }
        return operationOutList;
    }


}
