package order;

import com.fasterxml.jackson.core.JsonProcessingException;
import order.dto.OperationIn;
import order.dto.OperationOut;
import order.exception.NegativeValueException;

import java.io.IOException;
import java.util.List;

import static order.utils.UtilIOHandler.*;

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