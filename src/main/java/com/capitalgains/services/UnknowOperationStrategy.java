package com.capitalgains.services;

import com.capitalgains.dto.OperationCosts;
import com.capitalgains.dto.OperationIn;
import com.capitalgains.dto.OperationOut;

import java.math.BigDecimal;
import java.util.List;

public class UnknowOperationStrategy implements TaxStrategy{
    @Override
    public void handleOperation(OperationIn op, List<OperationOut> operationOutList, OperationCosts operationCosts) {
        operationOutList.add(new OperationOut(BigDecimal.ZERO));
    }
}
