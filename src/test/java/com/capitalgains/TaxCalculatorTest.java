package com.capitalgains;

import com.capitalgains.dto.OperationIn;
import com.capitalgains.dto.OperationOut;
import com.capitalgains.exception.NegativeValueException;
import com.capitalgains.services.CalculatorOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.capitalgains.utils.Validation.validateOperationsNegativeAndNulls;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaxCalculatorTest {
    private CalculatorOperation calculator;

    @BeforeEach
    public void setup() {
        calculator = new CalculatorOperation();
    }

    @Test
    public void testCase1() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", BigDecimal.valueOf(10.00), 100));
        operations.add(createOperation("sell", BigDecimal.valueOf(15.00), 50));
        operations.add(createOperation("sell", BigDecimal.valueOf(15.00), 50));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(0).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(1).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(2).getTax());
    }

    @Test
    public void testCase2() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", BigDecimal.valueOf(10.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(20.00), 5000));
        operations.add(createOperation("sell", BigDecimal.valueOf(5.00), 5000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(0).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(10000.00).setScale(2, RoundingMode.HALF_UP), results.get(1).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(2).getTax());
    }

    @Test
    public void testCase3() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", BigDecimal.valueOf(10.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(5.00), 5000));
        operations.add(createOperation("sell", BigDecimal.valueOf(20.00), 3000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(0).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(1).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP), results.get(2).getTax());
    }

    @Test
    public void testCase4() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", BigDecimal.valueOf(10.00), 10000));
        operations.add(createOperation("buy", BigDecimal.valueOf(25.00), 5000));
        operations.add(createOperation("sell", BigDecimal.valueOf(15.00), 10000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(0).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(1).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(2).getTax());
    }

    @Test
    public void testCase5() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", BigDecimal.valueOf(10.00), 10000));
        operations.add(createOperation("buy", BigDecimal.valueOf(25.00), 5000));
        operations.add(createOperation("sell", BigDecimal.valueOf(15.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(25.00), 5000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(4, results.size());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(0).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(1).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(2).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(10000.00).setScale(2, RoundingMode.HALF_UP), results.get(3).getTax());
    }

    @Test
    public void testCase6() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", BigDecimal.valueOf(10.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(2.00), 5000));
        operations.add(createOperation("sell", BigDecimal.valueOf(20.00), 2000));
        operations.add(createOperation("sell", BigDecimal.valueOf(20.00), 2000));
        operations.add(createOperation("sell", BigDecimal.valueOf(25.00), 1000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(5, results.size());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(0).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(1).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(2).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), results.get(3).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(3000.00).setScale(2, RoundingMode.HALF_UP), results.get(4).getTax());
    }

    @Test
    public void testCase7() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", BigDecimal.valueOf(10.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(2.00), 5000));
        operations.add(createOperation("sell", BigDecimal.valueOf(20.00), 2000));
        operations.add(createOperation("sell", BigDecimal.valueOf(20.00), 2000));
        operations.add(createOperation("sell", BigDecimal.valueOf(25.00), 1000));
        operations.add(createOperation("buy", BigDecimal.valueOf(20.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(15.00), 5000));
        operations.add(createOperation("sell", BigDecimal.valueOf(30.00), 4350));
        operations.add(createOperation("sell", BigDecimal.valueOf(30.00), 650));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(9, results.size());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(0).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(1).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(2).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP), results.get(3).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(3000.00).setScale(2, RoundingMode.HALF_UP), results.get(4).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(5).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(6).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(3700.00).setScale(2, RoundingMode.HALF_UP), results.get(7).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(8).getTax());
    }

    @Test
    public void testCase8() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", BigDecimal.valueOf(10.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(50.00), 10000));
        operations.add(createOperation("buy", BigDecimal.valueOf(20.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(50.00), 10000));

        List<OperationOut> results = calculator.calcTax(operations);

        Assertions.assertEquals(4, results.size());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(0).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(80000.00).setScale(2, RoundingMode.HALF_UP), results.get(1).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(0.00), results.get(2).getTax());
        Assertions.assertEquals(BigDecimal.valueOf(60000.00).setScale(2, RoundingMode.HALF_UP), results.get(3).getTax());
    }

    @Test
    public void testCase9() {
        List<OperationIn> operations = new ArrayList<>();
        operations.add(createOperation("buy", BigDecimal.valueOf(10.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(-9), 10000));
        operations.add(createOperation("buy", BigDecimal.valueOf(20.00), 10000));
        operations.add(createOperation("sell", BigDecimal.valueOf(50.00), 10000));

        NegativeValueException exception = assertThrows(NegativeValueException.class, ()->
                validateOperationsNegativeAndNulls(operations));

        assertTrue(exception.getMessage().contains("Unit cost cannot be negative or null"));
    }


    private OperationIn createOperation(String type, BigDecimal unitCost, int quantity) {
        OperationIn op = new OperationIn();
        op.setOperation(type);
        op.setUnitCost(unitCost);
        op.setQuantity(quantity);
        return op;
    }
}
