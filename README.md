# 💸 Payment Transfer System

This project was developed as part of a technical task for **NLB Digit d.o.o.**

## 📘 Overview

The Payment Transfer System is a simple digital banking application that allows users to:

- 🏦 Create bank accounts
- 🔁 Transfer funds between accounts

This project is developed using Java 17 and tested on Java 21. It uses PostgreSQL 17 running inside a Docker container.

Important:

**Before you begin, please note that the database is initially empty—no tables or data are present out of the box.**
**You will need to run the provided scripts or follow the instructions below to populate the database with the required**
**schema and execute the first transactions.**

## 🗄️ Database Schema Overview
The application uses a simple yet robust relational schema consisting of the following core tables:

1. **accounts**
   This table represents user or business accounts within the system. Each account has a unique identifier and maintains
   its current balance. It serves as the starting point for all financial operations.

2. **transactions**
   This table records high-level information about each financial operation (e.g., transfers between accounts). Each
   transaction is immutable and includes metadata such as timestamp, amount, and reference to involved accounts.

3. **ledger_entries**
   The ledger table contains the detailed line items that reflect debits and credits involved in each transaction. For
   every transaction, there will be at least two corresponding ledger entries: one debit and one credit. This design
   ensures double-entry accounting principles are followed, keeping the system auditable and consistent.

## 📄 Code References & Logic Highlights

- [`PaymentControllerImpl.java` - Payment Controller](https://github.com/borispopicbusiness/paymenttransfersystem/blob/4ca4a46a7ddafa0112c8e410ac8cca3d634c2f63/src/main/java/org/borispopic/paymenttransfersystem/controller/impl/PaymentControllerImpl.java#L51-L57)  
  Handles the `/api/v1/payment/execute` endpoint. Delegates the payment transfer request to the service layer and
  returns the appropriate HTTP response.

- [`PaymentExceptionHandler.java `- Exceptions Handling](https://github.com/borispopicbusiness/paymenttransfersystem/blob/1fe1744e51e290d5d9063d5b315810ec1d715c1c/src/main/java/org/borispopic/paymenttransfersystem/exception/handler/PaymentExceptionHandler.java#L14-L36)  
  Centralized exception handler that catches business rule violations (`BusinessException`) and returns meaningful
  error messages with proper status codes.

- [`TransactionServiceImpl.java` – Payment Logic](https://github.com/borispopicbusiness/paymenttransfersystem/blob/4ca4a46a7ddafa0112c8e410ac8cca3d634c2f63/src/main/java/org/borispopic/paymenttransfersystem/service/impl/TransactionServiceImpl.java#L50-L87)  
  Core logic for executing a payment: checks IBAN format, verifies balance, deducts and credits accounts, saves
  the transaction, and logs the operation.

- [`TransactionServiceImpl.java` – IBAN Validation](https://github.com/borispopicbusiness/paymenttransfersystem/blob/4ca4a46a7ddafa0112c8e410ac8cca3d634c2f63/src/main/java/org/borispopic/paymenttransfersystem/service/impl/TransactionServiceImpl.java#L129-L139)  
  Utility method that validates whether an account number conforms to the IBAN structure using a regular expression.

- [`TransactionServiceImplTest.java`- Mock test](https://github.com/borispopicbusiness/paymenttransfersystem/blob/1fe1744e51e290d5d9063d5b315810ec1d715c1c/src/test/java/org/borispopic/paymenttransfersystem/service/impl/TransactionServiceImplTest.java#L32-L323)
  Thorough unit test suite that leverages mocks to isolate and validate the behavior of the TransactionServiceImpl.
  It verifies core payment execution logic including IBAN validation, account balance checks, proper debit/credit 
  operations, and exception handling. This test ensures the service logic functions correctly in isolation from 
  external systems such as the database layer.

- [`TransactionServiceImplIntegrationTest` - Integration test](https://github.com/borispopicbusiness/paymenttransfersystem/blob/1fe1744e51e290d5d9063d5b315810ec1d715c1c/src/test/java/org/borispopic/paymenttransfersystem/service/impl/TransactionServiceImplIntegrationTest.java#L29-L133)
  Integration test that verifies the full payment transaction flow in a real application context, including database interactions.
  It ensures that a valid payment request results in account balance updates and transaction persistence, while also checking that 
  invalid inputs (e.g., malformed IBANs or insufficient balance) are handled gracefully according to the centralized PaymentExceptionHandler.

## 🚀 Getting Started

### 🐳 Docker Setup

Before running the application, ensure the necessary Docker containers are up and running. To do this, navigate to the `docker/` directory and use the following command to start all services in detached mode:

    cd docker
    docker-compose up -d

### 🛠️ Build and Run the Application

Once the Docker containers are up, you can proceed to build and run the application.

To clean and install the required dependencies, run the following Maven command:

    mvn clean install

To run the application use the following command:

    mvn spring-boot:run

### 🛠️ Tests

To run the tests, run the following the command:

    mvn test

### 🧾 Create Accounts

You can create accounts by sending POST requests to the `/api/v1/accounts/create` endpoint.

#### 🧑‍💼 Example: Create Account 1 (John Doe)

    curl -X POST "http://localhost:8080/api/v1/accounts/create" -H "Content-Type: application/json" -d '{
    "accountNumber": "GB29NWBK60161331926819",
    "balance": 1000.00,
    "currency": "GBP",
    "registered": "2025-04-09",
    "owner": "John Doe",
    "accountType": "COMMERCIAL",
    "comment": "Valid IBAN account"
    }'

#### 👩‍💼 Example: Create Account 2 (Jane Doe)

    curl -X POST "http://localhost:8080/api/v1/accounts/create" -H "Content-Type: application/json" -d '{
    "accountNumber": "DE89370400440532013000",
    "balance": 1500.00,
    "currency": "GBP",
    "registered": "2025-04-09",
    "owner": "Jane Doe",
    "accountType": "COMMERCIAL"
    }'

### 💳 Execute Payment Transfer

To transfer funds between accounts, send a POST request to `/api/v1/payment/execute`.

#### 🔄 Example: Transfer Payment

    curl -X POST "http://localhost:8080/api/v1/payment/execute" -H "Content-Type: application/json" -d '{
    "amount": 250.00,
    "sourceAccountId": "DE89370400440532013000",
    "destinationAccountId": "GB29NWBK60161331926819",
    "comment": "Payment for services"
}'

## 📝 Notes

- ✅ Account numbers must follow the IBAN format.
- 💰 The source account must have sufficient funds.
- 📦 All requests and responses use JSON format.
