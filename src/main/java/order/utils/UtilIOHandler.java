package order.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import order.dto.OperationIn;
import order.dto.OperationOut;
import order.exception.NegativeValueException;
import order.services.CalculatorOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static order.utils.Validation.validateOperationsNegativeAndNulls;

public class UtilIOHandler {

    public static List<OperationIn> readInput() throws IOException, NegativeValueException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder jsonInput = new StringBuilder();
        List<String> lines = new ArrayList<>();
        List<OperationIn> operationInList = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.isBlank() || line.isEmpty()) {
                break;
            }
            if (line.contains("]")) {
                jsonInput.append(line);
                lines.add(jsonInput.toString());
                jsonInput = new StringBuilder();
            } else {
                jsonInput.append(line);
            }
        }
        operationInList = parseJsonLines(lines);
        validateOperationsNegativeAndNulls(operationInList);
        return operationInList;
    }

    public static List<OperationIn> parseJsonLines(List<String> lines) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<OperationIn> operations = new ArrayList<>();

        for (String line : lines) {
            operations.addAll(mapper.readValue(line, new TypeReference<List<OperationIn>>() {}));
        }
        return operations;
    }

    public static List<OperationOut> calculateOperations(List<OperationIn> operations) {
        CalculatorOperation calculatorOperation = new CalculatorOperation();
        return calculatorOperation.calcTax(operations);
    }

    public static void printResults(List<OperationOut> operationOutList) {
        operationOutList.forEach(operationOut -> System.out.println(operationOut.toString()));
    }

}
