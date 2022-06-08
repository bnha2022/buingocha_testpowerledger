# Batteries Storing Application

A simple rest service that allows the storage of **Batteries** along with total and average watt capacity.

## Storing Batteries with Name, Postcode and Watt Capacity

You can store names & postcodes into this service by sending a `POST` request to `/batteries` of the server. The format should be in a JSON array with the child object's containing the respective **name** and **postcode** attributes. For example:

```json
[
	{ "name": "Tesla Model X", "postcode": 2000, "wattCapacity": 1500 },
	{ "name": "Anker", "postcode": 3000, "wattCapacity": 2000 },
	{ "name": "Aukey", "postcode": 3500, "wattCapacity": 3580 },
	{ "name": "Energizer", "postcode": 4000, "wattCapacity": 4500 }
]
```

Batteries names are also to be between 2 and 32 characters long.

## Fetching Batteries information by Postcode Range

To test the APIs, I created Swagger UI link below 

```sh
    http://localhost:8080/swagger-ui.html
```

For example, with API GET data batteries 
```sh
    curl -X GET "http://localhost:8080/batteries?postcodeEnd=4500&postcodeStart=2000"
```
We will receive response below

```json
{
  "names": [
    "Anker",
    "Aukey",
    "Energizer",
    "Tesla Model X"
  ],
  "totalWattCapacity": 11580,
  "averageWattCapacity": 2895
}
```