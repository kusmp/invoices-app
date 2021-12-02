# Stripe Api Application

The app uses the Stripe API to get and add invoices to the system.

## Libraries and tools

- Java 11
- Spring Boot
- Spring Security
- Maven
- Lombok
- Mockito
- Sl4j

## Application endpoints

GET `http://localhost:8080/v1/{invoiceId}` - get althe invoice by id

POST `localhost:8080/v1/user` - create new customer

POST `localhost:8080/v1/` - save new invoice

Example POST request:
`{
"customer": "cus_KhcpGwb3BfFAFd",
"invoiceItem" : {
"customer": "cus_KhcpGwb3BfFAFd",
"amount" :100,
"currency": "pln"
} }`

Be informed that the customer field is mandatory. If you omit it, your request will not pass validation.

To run this application you must enter your api key.



