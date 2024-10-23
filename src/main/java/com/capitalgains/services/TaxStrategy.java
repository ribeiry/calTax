package com.capitalgains.services;

import com.capitalgains.dto.OperationCosts;
import com.capitalgains.dto.OperationIn;
import com.capitalgains.dto.OperationOut;

import java.util.List;

public interface TaxStrategy {
    void handleOperation(OperationIn op, List<OperationOut> operationOuts, OperationCosts operationCosts);
}
