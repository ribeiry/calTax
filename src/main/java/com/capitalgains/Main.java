package com.capitalgains;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.capitalgains.dto.OperationIn;
import com.capitalgains.dto.OperationOut;
import com.capitalgains.exception.NegativeValueException;

import java.io.IOException;
import java.util.List;

import static com.capitalgains.utils.UtilIOHandler.*;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {

        try {
            List<OperationIn> operations = readInput();
            List<OperationOut> operationOutList = calculateOperations(operations);
            printResults(operationOutList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NegativeValueException e) {
            throw new RuntimeException(e);
        }
    }
}