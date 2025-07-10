# TravelLight
A simple Java Spring Boot CLI application demonstrating a **Simplified Business Model** of **LittlePay**.

A sample output CSV, output_e2e.csv generated when a sample input CSV, input_e2e.csv is processed, 
is included in the `src/test/resources` directory.

Sample input CSV, input_e2e.csv:
```csv
ID,DateTimeUTC,TapType,StopId,CompanyId,BusID, PAN
1,22/01/2023 13:00,ON,Stop1,Company1,Bus37,5500010000000000
2,22/01/2023 13:05,OFF,Stop2,Company1,Bus37,5500010000000000
3,22/01/2023 9:20,ON,Stop3,Company1,Bus36,4111110000000000
4,23/01/2023 8:00,ON,Stop1,Company1,Bus37,4111110000000000
5,23/01/2023 8:02,OFF,Stop1,Company1,Bus37,4111110000000000
6,24/01/2023 16:30,OFF,Stop2,Company1,Bus37,5500010000000000
```

Sample output CSV, output_e2e.csv:
```csv
Started,Finished,DurationSecs,FromStopId,ToStopId,ChargeAmount,CompanyId,BusID,PAN,Status
2023-01-22T09:20,N/A,N/A,Stop3,N/A,7.3,Company1,Bus36,4111110000000000,INCOMPLETE
2023-01-23T08:00,2023-01-23T08:02,120,Stop1,Stop1,0.0,Company1,Bus37,4111110000000000,CANCELLED
2023-01-22T13:00,2023-01-22T13:05,300,Stop1,Stop2,3.25,Company1,Bus37,5500010000000000,COMPLETE
,2023-01-24T16:30,N/A,Stop2,N/A,5.5,Company1,Bus37,5500010000000000,INCOMPLETE
```
---
# Prerequisites
- Java 23 or higher
- Maven 3.9.4 or higher
---
# How to run the application
Include the input CSV file in the `src/main/resources` directory. 
The file should be named `input.csv`.

1. If running from the command line, navigate to the root directory of the project and run:
```bash
mvn clean package
java -jar target/travel_light-1.0-SNAPSHOT.jar
```
2. If running from an IDE, run the main method in the `com.travellight.TravelLightApplication` class.
---
# How to run the tests
Include the input CSV file in the `src/test/resources` directory.
The file should be named `input.csv`.

1. If running from the command line, navigate to the root directory of the project and run:
```bash
mvn clean test
```
2. If running from an IDE, run the test classes in the `com.travellight` package.

You can also run the tests individually by running the main method in each test class.

---
# Assumptions
- Since this is currently implemented as a CLI application, the input CSV file is expected to be in the `src/main/resources` directory
  and the output CSV file will be generated in the same directory.
- Also, there's no user authorization or authentication implemented, so the application is assumed to be run by an authorized user.
- All input data is assumed to be valid and well-formed.
- A tap OFF with no matching tap ON is considered an incomplete trip.
- Fields that cannot be determined for Incomplete trips such as `Finished`, `DurationSecs`, and `ToStopId` are set to `N/A`.
- In the interest of time, not all real-world scenarios are covered, such as existance of multiple routes passing through the same stop, 
  or multiple buses operating on the same route.
- For the same reason as above, not all possible unit tests have been written to meet coverage requirements. The provided tests cover the main scenarios and an idea of the tests written.
---
# Improvements
- The application is designed to be simple and easy to understand, focusing on the core business logic.
- It can be extended to include more features, such as user authentication, data validation, and error handling.
- The true capabilities of Spring Boot, such as dependency injection, AOP, and transaction management, are not fully utilized in this simple CLI application.
- Scalability and performance in a real world application can be explored by developing into a Restful API or a microservice by adding Rest Controllers to consume the API requests.
- By hosting the application on a cloud platform like AWS, it can be made highly available and scalable.
- Custom exceptions can be added to cover common error scenarios, such as data issues, IO issues or business logic errors which can be used to provide meaningful error messages to the user.