# Spring Boot 3 with MongoDb

In this repository we see how we can communicate with MongoDB using Spring Data MongoDB

### Sample Post Request
```shell
curl --location --request POST 'http://localhost:8080/product' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productName" : "Macbook Pro",
    "price" : "1234.456",
    "attributes" :{
        "CPU" : "M1",
        "Model" : "X51",
        "Hard Disk": "512GB"
    }

}'
```