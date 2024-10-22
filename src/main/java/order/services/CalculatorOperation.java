package order.services;

import order.dto.OperationIn;
import order.dto.OperationOut;

import java.util.ArrayList;
import java.util.List;

public class CalculatorOperation {

    public List<OperationOut> calcTax(List<OperationIn> operationInList){
        List<OperationOut> operationOutList = new ArrayList<>();
        List<Boolean> impostoOperationList  = new ArrayList<Boolean>();
        Double[]  calcPrejuizo = {0.0};
        List<Double> taxList = new ArrayList<>();
        Double medPonderada  = calcMediaPonderada(operationInList);

        operationInList.stream().forEach(operation-> {
            if (operation.getOperation().equalsIgnoreCase("sell")) {
                impostoOperationList.add(calcOperation(operation.getQuantity(),operation.getUnitCost()));

            }
        });

        operationInList.stream().forEach(operation-> {
            if(operation.getOperation().equalsIgnoreCase("sell"))
                if(calcOperation(operation.getQuantity(),operation.getUnitCost()))
                    if(calcPrejuizo(operation.getUnitCost(),medPonderada,operation.getQuantity()) < 0 )
                        taxList.add(0.0);
                    else
                        if(operation.getUnitCost() > medPonderada)
                            taxList.add((((operation.getUnitCost() * operation.getQuantity()) - (operation.getQuantity() * medPonderada))
                                    - calcPrejuizo(operation.getUnitCost(),medPonderada,operation.getQuantity())) *0.20);

                        else
                            taxList.add((((operation.getUnitCost() * operation.getQuantity()) - (operation.getQuantity() * medPonderada))) *0.20);
                else
                    taxList.add(0.0);
            else
                taxList.add(0.0);
        });

        taxList.stream().forEach(tax -> {
            OperationOut operationOut = new OperationOut();
            operationOut.setTax(tax);
            operationOutList.add(operationOut);
        });

        return operationOutList;
    }

    private boolean calcOperation(int qtde, Double costUnit){

        return  (costUnit * qtde) >= 20000.00;

    }

//    private Double calcPrecoMedioPonderado(List<OperationIn> operationInList){
//        int[] qtdeStockBuy  = {0};
//        int[] qtdeStockSell = {0};
//        Double[] costBuy    = {0.0};
//        Double medPonderada = calcMediaPonderada(operationInList);
//
//        operationInList.stream().forEach(operation->{
//            if(operation.getOperation().equalsIgnoreCase("buy")) {
//                qtdeStockBuy[0] += operation.getQuantity();
//                costBuy[0]      += operation.getUnitCost();
//            }
//            else{
//                qtdeStockSell[0] += operation.getQuantity();
//            }
//        });
//
//
//        int qtdeStocks = qtdeStockBuy[0] - qtdeStockSell[0];
//       // ((quantidade-de-ações-atual * média-ponderada-atual) + (quantidade-de-ações-compradas * valor-de-compra)) / (quantidade-de-ações-atual + quantidade-de-ações-compradas)
//        return ((qtdeStocks * medPonderada) + (qtdeStockBuy[0] * costBuy[0]))/(qtdeStocks+qtdeStockBuy[0]);
//    }

    private Double calcPrejuizo(Double precoUnidade, Double costAverage,int qtde){
        return (precoUnidade - costAverage) * qtde;

    }

    private Double calcMediaPonderada(List<OperationIn> operationInList){

       List<Integer> stockBuy = new ArrayList<>();
       List<Double> qtdeValue = new ArrayList<>();
       int[] qtde = {0};
       Double[] qtdeCost = {0.0};
        operationInList.stream().forEach(operation->{
            if(operation.getOperation().equalsIgnoreCase("buy")) {
                qtdeValue.add(operation.getQuantity() * operation.getUnitCost());
                qtde[0] += operation.getQuantity();
            }
        });

        qtdeValue.forEach(custo -> {
            qtdeCost[0] += custo;
        });

        return qtdeCost[0]/qtde[0];

    }

}
