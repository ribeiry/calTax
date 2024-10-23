# Capital Gains Calculator

## Visão Geral

Este projeto implementa um programa de linha de comando (CLI) para calcular o imposto sobre lucros ou prejuízos de operações no mercado financeiro de ações. O programa recebe uma lista de operações em formato JSON através da entrada padrão (stdin) e retorna uma lista de impostos pagos para cada operação na saída padrão (stdout).

## Decisões Técnicas e Arquiteturais

- **Linguagem:** Java 17
- **Gerenciamento de Dependências:** Maven
- **Processamento de JSON:** Jackson
- **Testes:** JUnit 5

As operações e os resultados dos impostos são representados por classes modelo (`OperationIn`,`OperationOut` e `OperationCosts`).
A classe `CalculatorOperation` na service chama as strategies que sāo as seguintes classes:
- `BuyOperationStrategy` classe responsavel pela compra e atualizar o preço médio;
- `SellOperation` calcula todos os calculos de prejuizo e abatimento deles para o calculo da taxa;
- `UnknowOperationStrategy` quando nāo é reconhecida uma operaçāo ele entra na como unknow e é retornado como 0.

Já no pacote **exception** temos a `NegativeValueException` para que seja lançada quando for identificados numeros negativos ou nulos.
Outro pacote importante é o **utils** que contém a classe `Validation` que efetua validações numeros nulos ou negativos e temos também a 
classe `UtilIOHandler` que é responsavel por efetuar a leitura do console.

## Justificativa para Uso de Bibliotecas

- **Jackson:** Facilita o mapeamento de JSON para objetos Java e vice-versa, tornando o processamento de entrada e saída mais eficiente.
- **JUnit 5:** Oferece um framework robusto para escrever e executar testes de unidade, garantindo a confiabilidade da lógica de negócios.

## Como Compilar e Executar o Projeto

### Requisitos

- **Java 17** ou superior
- **Maven** instalado

### Passos

1. **Clone o Repositório:**

   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd capital-gains

	2.	Compilar o Projeto:
Execute o seguinte comando na raiz do projeto para compilar o código:

mvn clean compile


	3.	Executar os Testes:
Para executar os testes de unidade:

mvn test


	4.	Empacotar o Projeto:
Para criar um arquivo JAR executável:

mvn package

O JAR será gerado em target/capital-gains-1.0.0.jar.

	5.	Executar a Aplicação:
Você pode executar a aplicação passando um arquivo de entrada ou digitando manualmente:
•	Usando Redirecionamento de Arquivo:

java -jar target/capital-gains-1.0.0.jar < input.txt


	•	Digitando Manualmente:
Execute o JAR e insira as linhas JSON, pressionando Enter após cada linha. Para finalizar, insira uma linha vazia.

java -jar target/capital-gains-1.0.0.jar



Executando os Testes

Os testes estão localizados no diretório src/test/java/com/nubank/capitalgains/service/CalculatorOperationInTest.java. Para executar os testes, use o comando:

mvn test

Os resultados dos testes serão exibidos no console, indicando quais testes passaram ou falharam.


## 7. Executando a Aplicação

### Exemplo de Entrada e Saída

**Entrada (`input.txt`):**

```json
[{"OperationIn":"buy", "unit-cost":10.00, "quantity":10000},
{"OperationIn":"sell", "unit-cost":20.00, "quantity":5000},
{"OperationIn":"sell", "unit-cost":5.00, "quantity":5000}]
[{"OperationIn":"buy", "unit-cost":10.00, "quantity":100},
{"OperationIn":"sell", "unit-cost":15.00, "quantity":50},
{"OperationIn":"sell", "unit-cost":15.00, "quantity":50}]

Comando de Execução:

java -jar target/capital-gains-1.0.0.jar < input.txt

Saída:

[{"tax":0.00},{"tax":10000.00},{"tax":0.00}]
[{"tax":0.00},{"tax":0.00},{"tax":0.00}]
