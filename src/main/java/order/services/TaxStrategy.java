package order.services;

import order.dto.OperationIn;
import order.dto.OperationOut;

import java.util.List;

public interface TaxStrategy {
    void handleOperation(OperationIn op, List<OperationOut> operationOuts);
}
