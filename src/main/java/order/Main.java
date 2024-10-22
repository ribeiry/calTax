package order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import order.dto.OperationIn;
import order.services.CalculatorOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
       try{
           CalculatorOperation calculatorOperation = new CalculatorOperation();
           BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
           StringBuilder jsonInput = new StringBuilder();
           ObjectMapper mapper = new ObjectMapper();
           String line ;

           while ((line = reader.readLine()) != null){
               if(line.isBlank() || line.isEmpty()){
                   break;
               }
               jsonInput.append(line);

           }
           List<OperationIn> operationInList =  mapper.readValue(jsonInput.toString(),
                            new TypeReference<List<OperationIn>>(){});

           calculatorOperation.calcTax(operationInList).forEach(operationOut -> {
               System.out.println(operationOut.tax.toString());
           });



       }
       catch (IOException e) {
           throw new RuntimeException(e);
       }
    }
}