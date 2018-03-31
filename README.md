# marketplace


Self-employed Marketplace REST APIs built using Spring Boot.


## Design Considerations
The solution for building marketplace for self-employed involves designing and implementing REST APIs to serve sellers and taskers(buyers). The Instead it uses a single database to  The application is built using spring boot, spring data/JPA/hibernate and supports multiple profiles. The application uses in-memory database - H2 for local development and testing. This development/test/production environments could use a database such as PostgreSQL or other alternatives.  The APIs are documented using swagger.

**Domain Model**
    User - a user in the marketplace. The user can act as seller and buyer
    Project - a project that seller wishes to complete with the help of taskers
    Bid - Buyers who are interested in bidding could offer a solution with a cost.

**Application flow**

1. User registration - POST /api/v1/users
2. Post new project - POST /api/v1/projects
3. Offer a solution - POST /api/v1/bids
		- if the new bid is lowest, project's lowest bid field is updated with new Bid Model
4. When the project deadline is reached, bids are no longer allowed. The bid with lowest amount is selected for implementation. Project status shows BID_COMPLETED at this stage.

**Specific Considerations**

1. Tracking lowest bid
		 The Project domain has a specific field to capture the lowest bid. This will be updated every time a new bid is posted/updated/deleted.
2. Project deadline - awarding lowest bid
		 In order to avoid scheduling jobs that updates the project that has completed deadlines, it uses the lowest bid field to find the bid that will be awarded the work. This is under the assumption that the lowest bid will be awarded the work and no other post processing tasks (eg. sending emails to the bidder) is needed on project completion. This helps the application to scale better by spawning multiple instances and avoid overhead of scheduling the jobs to update the project tables. Project has a pseudo field for holding the status which is based on the project deadline date.

**Microservices architecture**
	The solution uses microservices architecture in broad sense and does not host separate microservices for projects, bids and users. Such a solution would be recommended if the bid volume exceed few thousands per second. If the system load increases to such a scale, the database joins would pose a bottleneck and we would need to split the APIs into multiple microservices ( Project API, Bid API) and denormalize the database. This would require asynchronous messaging between the services to achieve eventual consistency.

**Patterns**
	The solution uses Front Controller, DTO, Business (Service) delegate, singleton patterns and uses Controller/Service/Repository stereotypes to segregate different layers of the application.

**Error Handling**
	The API would return http status code 200 OK for successful operations and include the latest the domain model that was updated. For validation errors, a 400 Bad request will be returned with the details of the fields that need to be corrected. A unhandled request will result in 500 Internal server error. In the error scenarios, a tracking ID will be returned in the response.

##Non-functional aspects

**Authentication**
	The user API generates an API key when a new user is registered. The API key will be passed in every request from the user to manage projects/bids. The current implementation uses userid from the request to identify and validate the user. The APIs could be OAuth enabled to handle the security aspect in a better way.

**Scalability**
	The application is self contained and ready for multiple instances of the application to be deployed.

**Availability**
	The application's health could be monitored using /health and instances could be rotated as needed.

**API versioning**
	The API uses versioning (/api/v1/..) and supports updation of API semantics with ease. A breaking change would follow as new release version.

**Domain model/DTOs**
	The domain models are converted to DTO (data transfer objects) when a REST API response is sent. This avoids data leakage to the external world and secures the data model. The DTOs could have been avoided if the API usage is restricted to internal use which abides by the DRY pinciple.

**Auditing/Optimistic locking**
	The domain models in the database has two fields for capturing the creation time and last updation time. A version field has been added to enable optimistic locking to avoid data overwrites in concurrent API calls.

**Project implementation**
	The application exposes APIs and will honor the requests as mentioned above. The validation scenarios and corner cases are not fully tested, mainly due to lack of time, and may run into issues.


## REST APIs

Projects        - /api/v1/projects
Bids            - /api/v1/bids
Buyers/Sellers  - /api/v1/users

Swagger API spec    :-  http://localhost:8080/v2/api-docs
Swagger UI          :- http://localhost:8080/swagger-ui.html

Projects API:-
![Alt text](/screenshots/Bids.png?raw=true "Projects API")

Bids API:-
![Alt text](/screenshots/Bids.png?raw=true "Bids API")

Users API:-
![Alt text](/screenshots/Bids.png?raw=true "Users API")

Example response:-	GET /api/v1/projects
```
	[
    {
        "id": 1,
        "name": "name1",
        "description": "desc1+2",
        "maxAmount": 54.45,
        "startDate": "2017-04-28T00:32:03Z",
        "deadline": "2017-09-17T22:47:52.690Z",
        "status": "BID_COMPLETED",
        "lowestBid": {
            "id": 1,
            "name": "bid1",
            "description": "bid1-desc",
            "bidAmount": 1
        },
        "minBidAmt": 1,
        "bids": [
            {
                "id": 1,
                "name": "bid1",
                "description": "bid1-desc",
                "bidAmount": 1
            },
            {
                "id": 2,
                "name": "bid2",
                "description": "bid2-desc",
                "bidAmount": 50
            }
        ]
    }
]
```

## Build/deployment steps

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

mvn clean install

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `de.codecentric.springbootsample.Application` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

