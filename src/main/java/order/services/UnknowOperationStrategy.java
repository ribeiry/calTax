package order.services;

import order.dto.OperationIn;
import order.dto.OperationOut;

import java.math.BigDecimal;
import java.util.List;

public class UnknowOperationStrategy implements TaxStrategy{
    @Override
    public void handleOperation(OperationIn op, List<OperationOut> operationOutList) {
        operationOutList.add(new OperationOut(BigDecimal.ZERO));
    }
}
