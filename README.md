
# **Movie Theater Seating Challenge**
## Assumptions
1. My solution assumes that customers don't have a preference for where they sit in the theater.
2. My solution assumes that sitting in the back of the theater is more favorable than sitting in the front of the theater.
3. My solution assumes that when people in the same reservation can't all sit together, it is most optimal to sit as many people as possible together
4. My solution assumes that it is optimal to maximize the number of people that can sit in a row.
## Prerequisites
* OpenJDK version 11.0.12+
* Maven version 12.1+

## **Setup + How to run the program**
1. Clone repository locally
2. Run the following commands in order:


   **Execute both of these commands from /MovieTheaterSeating**

> mvn clean

   **Specify both the input and output files in the next command**

   > mvn compile exec:java -Dexec.mainClass="Main" exec.args="path_to_input_file.txt path_to_output_file.txt"
4. Upon running the second command, the output file specified will contain the seating assignments for each reservation request.

## How to run JUnit test cases
1. In order to run the JUnit test cases, execute the following command:
   
   
   **Execute this command from /MovieTheaterSeating**
   > mvn test




