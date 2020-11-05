# Time Travel Assignment 

This is an API which allows Space-Time Travel. A traveller submits their travel details:
- Personal Galactic Identifier (PGI) which needs to be alphanumeric, start with a letter and between 5-10 characters in total
- Place
- Date

A traveller cannot make a journey to the same place and date twice.

## Assumptions
It was stated that a traveller could not travel to the same place and date twice. This is adhered to. However, the following assumptions were made:
- It is assumed that a traveller can go to a different place at the same time (after all it is a time machine).
- It is assumed that a traveller can go to the same place at a different time.

## Future Enhancements 

The API could be built out further by:
- providing PUT, DELETE endpoints as necessary.
- Introducing a physical database rather than the in memory implementation provided.
- The domain could be made stronger by creating types such as 'PersonalGalacticIdentifier' and 'JourneyId' rather than using primitives such as Strings.

## Getting the code

The API is implemented using the Spring Boot framework and built via maven.
Maven wrappers are included in the repository so you do not need to download it locally.

Clone the code: 
```
git clone git@github.com:gemcfadyen/time-travel-assignment.git
```

Navigate to the root directory:
```
cd time-travel-assignment
```

Build the code and run the tests:

For macOS/Linux:
```
   ./mvnw clean install 
```

For Windows:
```
   ./mvnw.cmd clean install
```

## Starting the service

The API can be started by either:

- Running the main method of `JourneysApplication.java` in your preferred IDE. 

- Running in the terminal:

For macOS/Linux:
```
    ./mvnw spring-boot:run
```

For Windows:
```
    ./mvnw.cmd spring-boot:run
```

## Database 

An in memory repository is provided. 

## API Documentation

When running locally, the swagger documentation can be browsed at `http://localhost:8081/swagger-ui.html` which details the available POST and GET endpoints.
