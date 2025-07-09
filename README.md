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

---
## Assumptions

---
## Questions
- Can I consider all the data in the input CSV including PANs as valid?
- What is identified by a "Company"?
- What happens if the user taps ON day 1 and then taps ON day 2 without tapping OFF on day 1?
- How should the output file be ordered?
- Does the leg list include all possible combinations of legs along a route?
- Is the data in the input CSV always ordered by some criteria?
- If the user taps ON in one bus and taps ON in another bus without tapping OFF in the first, will the second tap always 
  considered a tap ON?
- What's the scenario for index 6 in the input CSV?

---

