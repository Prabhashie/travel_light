# TravelLight
A simple Java Spring Boot CLI application demonstrating a **Simplified Business Model** of **LittlePay**.

---
## Prerequisites
- Java 23 or higher
- Maven 3.9.4 or higher
---
## How to run the application
Include the input CSV file in the `src/main/resources` directory. 
The file should be named `input.csv`.

1. If running from the command line, navigate to the root directory of the project and run:
```bash
mvn clean package
java -jar target/travel_light-1.0-SNAPSHOT.jar
```
2. If running from an IDE, run the main method in the `com.travellight.TravelLightApplication` class.
---
## How to run the tests
Include the input CSV file in the `src/test/resources` directory.
The file should be named `input.csv`.

1. If running from the command line, navigate to the root directory of the project and run:
```bash
mvn clean test
```
2. If running from an IDE, run the test classes in the `com.travellight` package.

You can also run the tests individually by running the main method in each test class.

---
## Assumptions
- Since this is currently implemented as a CLI application, the input CSV file is expected to be in the `src/main/resources` directory
  and the output CSV file will be generated in the same directory.
- Also, there's no user authorization or authentication implemented, so the application is assumed to be run by an authorized user.
---
## Questions
- Does the leg list include all possible combinations of legs along a route?
- Can I consider all the data in the input CSV including PANs as valid?
- Is the data in the input CSV always ordered by some criteria?
- What is identified by a "Company"?
- What's the scenario for index 6 in the input CSV?
- How should the output file be ordered?
- What happens if the user taps ON day 1 and then taps ON day 2 without tapping OFF on day 1?
- If the user taps ON in one bus and taps ON in another bus without tapping OFF in the first, will the second tap always 
  considered a tap ON?
- What if the user touches ON the same bus several days apart?
- Is a trip distinguished by the bus and stop combination even when multiple buses on the same route?
- Duration of an incomplete trip?
---

