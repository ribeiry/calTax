package com.capitalgains;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.capitalgains.dto.OperationIn;
import com.capitalgains.dto.OperationOut;
import com.capitalgains.exception.NegativeValueOrNullException;

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
            System.out.println("Error ao ler o input:" + e.getMessage());
        } catch (NegativeValueOrNullException e) {
            System.out.println(e.getMessage());
        }
    }
}