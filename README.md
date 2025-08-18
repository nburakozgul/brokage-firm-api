# brokage-firm-api
Rest api example with Spring Boot and H2

## How to Start
- Run 'mvn clean install' on project folder /BrokageFirmProject
- Then run the application api will be ready on localhost:8080

## Authentication
### Basic Auth

User : user

Password : password

## Resources

- ```/api/v1``` -Main route

- ```GET /order/12``` - Retrieves order with orderId
- ```GET /order?customerId=?``` - Retrieves order with customerId
- ```POST /order``` - Creates a new order
- ```DELETE /order/12``` - Cancel order with orderId


- ```GET /asset/12``` - Retrieves asset with assetId
- ```GET /asset?customerId=?``` - Retrieves asset with customerId
- ```POST /asset``` - Creates a new asset
- ```DELETE /asset/12``` - Deletes asset with assetId

## Postman Documentation

Endpoints can be tried on postman collection: https://documenter.getpostman.com/view/732072/2sB3BKDSuf