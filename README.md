# InventoryApplication

Run the applcation using the below command
```sh
$ mvn spring-boot:run
```

To create a new inventory item :
```sh
$ curl -d '{"name":"item","releaseDate":"2019-02-06T08:01:54Z","manufacturer":{"name":"test","homePage":"http://www.google.com","phone":"(07) 5556 4321"}}' -H "Content-Type: application/json" -X POST --user test:1234  http://localhost:8080/inventory/

```

To retrieve all inventory items :
```sh
$ curl  --user test:1234  http://localhost:8080/inventory/
```
