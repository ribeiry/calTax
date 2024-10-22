package order;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import order.dto.OperationIn;
import order.dto.OperationOut;
import order.services.CalculatorOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
       try{
           CalculatorOperation calculatorOperation = new CalculatorOperation();
           BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
           StringBuilder jsonInput = new StringBuilder();
           ObjectMapper mapper = new ObjectMapper();
           String line ;
           List<String> lines = new ArrayList<>();

           while ((line = reader.readLine()) != null){
               if(line.isBlank() || line.isEmpty()){
                   break;
               }
               //tratamento de mais de um json
               if(line.contains("]")) {
                   jsonInput.append(line);
                   lines.add(jsonInput.toString());
                   jsonInput = new StringBuilder();
               }
               else {
                   jsonInput.append(line);
               }

           }

           List<OperationOut> operationOutList = new ArrayList<>();
           for (String operation : lines){
               operationOutList = calculatorOperation.calcTax((
                       mapper.readValue(operation.toString(),
                               new TypeReference<List<OperationIn>>(){})
               ));
           }

           operationOutList.forEach(operationOut -> {
               System.out.println(operationOut.toString());
           });


       }
       catch (IOException e) {
           throw new RuntimeException(e);
       }
    }
}