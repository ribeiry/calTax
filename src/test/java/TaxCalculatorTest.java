package order.test;

import order.dto.OperationIn;
import order.dto.OperationOut;
import order.services.CalculatorOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TaxCalculatorTest {
    private CalculatorOperation calculator;

    @BeforeEach
    public void setup() {
        calculator = new CalculatorOperation();
    }

    @Test
    public void testCase1() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", 10.00, 100));
        operations.add(createOperation("sell", 15.00, 50));
        operations.add(createOperation("sell", 15.00, 50));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(0.00, results.get(0).getTax());
        Assertions.assertEquals(0.00, results.get(1).getTax());
        Assertions.assertEquals(0.00, results.get(2).getTax());
    }

    @Test
    public void testCase2() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", 10.00, 10000));
        operations.add(createOperation("sell", 20.00, 5000));
        operations.add(createOperation("sell", 5.00, 5000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(0.00, results.get(0).getTax());
        Assertions.assertEquals(10000.00, results.get(1).getTax());
        Assertions.assertEquals(0.00, results.get(2).getTax());
    }

    @Test
    public void testCase3() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", 10.00, 10000));
        operations.add(createOperation("sell", 5.00, 5000));
        operations.add(createOperation("sell", 20.00, 3000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(0.00, results.get(0).getTax());
        Assertions.assertEquals(0.00, results.get(1).getTax());
        Assertions.assertEquals(1000.00, results.get(2).getTax());
    }

    @Test
    public void testCase4() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", 10.00, 10000));
        operations.add(createOperation("buy", 25.00, 5000));
        operations.add(createOperation("sell", 15.00, 10000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(0.00, results.get(0).getTax());
        Assertions.assertEquals(0.00, results.get(1).getTax());
        Assertions.assertEquals(0.00, results.get(2).getTax());
    }

    @Test
    public void testCase5() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", 10.00, 10000));
        operations.add(createOperation("buy", 25.00, 5000));
        operations.add(createOperation("sell", 15.00, 10000));
        operations.add(createOperation("sell", 25.00, 5000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(4, results.size());
        Assertions.assertEquals(0.00, results.get(0).getTax());
        Assertions.assertEquals(0.00, results.get(1).getTax());
        Assertions.assertEquals(0.00, results.get(2).getTax());
        Assertions.assertEquals(10000.00, results.get(3).getTax());
    }

    @Test
    public void testCase6() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", 10.00, 10000));
        operations.add(createOperation("sell", 2.00, 5000));
        operations.add(createOperation("sell", 20.00, 2000));
        operations.add(createOperation("sell", 20.00, 2000));
        operations.add(createOperation("sell", 25.00, 1000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(5, results.size());
        Assertions.assertEquals(0.00, results.get(0).getTax());
        Assertions.assertEquals(0.00, results.get(1).getTax());
        Assertions.assertEquals(0.00, results.get(2).getTax());
        Assertions.assertEquals(0.00, results.get(3).getTax());
        Assertions.assertEquals(3000.00, results.get(4).getTax());
    }

    @Test
    public void testCase7() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", 10.00, 10000));
        operations.add(createOperation("sell", 2.00, 5000));
        operations.add(createOperation("sell", 20.00, 2000));
        operations.add(createOperation("sell", 20.00, 2000));
        operations.add(createOperation("sell", 25.00, 1000));
        operations.add(createOperation("buy", 20.00, 10000));
        operations.add(createOperation("sell", 15.00, 5000));
        operations.add(createOperation("sell", 30.00, 4350));
        operations.add(createOperation("sell", 30.00, 650));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(9, results.size());
        Assertions.assertEquals(0.00, results.get(0).getTax());
        Assertions.assertEquals(0.00, results.get(1).getTax());
        Assertions.assertEquals(0.00, results.get(2).getTax());
        Assertions.assertEquals(0.00, results.get(3).getTax());
        Assertions.assertEquals(3000.00, results.get(4).getTax());
        Assertions.assertEquals(0.00, results.get(5).getTax());
        Assertions.assertEquals(0.00, results.get(6).getTax());
        Assertions.assertEquals(3700.00, results.get(7).getTax());
        Assertions.assertEquals(0.00, results.get(8).getTax());
    }

    @Test
    public void testCase8() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", 10.00, 10000));
        operations.add(createOperation("sell", 50.00, 10000));
        operations.add(createOperation("buy", 20.00, 10000));
        operations.add(createOperation("sell", 50.00, 10000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(4, results.size());
        Assertions.assertEquals(0.00, results.get(0).getTax());
        Assertions.assertEquals(80000.00, results.get(1).getTax());
        Assertions.assertEquals(0.00, results.get(2).getTax());
        Assertions.assertEquals(60000.00, results.get(3).getTax());
    }

    private OperationIn createOperation(String type, double unitCost, int quantity) {
        OperationIn op = new OperationIn();
        op.setOperation(type);
        op.setUnitCost(unitCost);
        op.setQuantity(quantity);
        return op;
    }
}
