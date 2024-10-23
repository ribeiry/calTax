package com.capitalgains.services;

import com.capitalgains.dto.OperationCosts;
import com.capitalgains.dto.OperationIn;
import com.capitalgains.dto.OperationOut;

import java.math.BigDecimal;
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
    private OperationCosts operationCosts = new OperationCosts(averageCost,totalQuantity,cumulativeLoss);
    private List<OperationOut> operationOutList = new ArrayList<>();

    private final Map<String, TaxStrategy> strategyMap = new HashMap<>();

    public CalculatorOperation() {
        strategyMap.put(OPERATION_BUY, new BuyOperationStrategy());
        strategyMap.put(OPERATION_SELL, new SellOperationStrategy());
        strategyMap.put("unknown", new UnknowOperationStrategy());
    }

    public List<OperationOut> calcTax(List<OperationIn> operationInList) {
        for (OperationIn op : operationInList) {
            TaxStrategy strategy = strategyMap.getOrDefault(op.getOperation().toLowerCase(), strategyMap.get("unknown"));
            strategy.handleOperation(op, operationOutList,operationCosts);
        }
        return operationOutList;
    }


}
