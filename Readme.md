# Batteries Storing Application

A simple rest service that allows the storage of **Batteries** along with total and average watt capacity.

## Storing Batteries with Name, Postcode and Watt Capacity

You can store names & postcodes into this service by sending a `POST` request to `/batteries` of the server. The format should be in a JSON array with the child object's containing the respective **name** and **postcode** attributes. For example:

```json
[
	{ "batteryName": "TestLa Model X", "postcode": 3433, "wattCapacity": 150 },
	{ "batteryName": "Anker", "postcode": 6045, "wattCapacity": 100 },
	{ "batteryName": "Energizer", "postcode": 6033, "wattCapacity": 120 },
	{ "batteryName": "Aukey", "postcode": 6412, "wattCapacity": 50 }
]
```

Batteries names are also to be between 2 and 32 characters long.

## Fetching Batteries information by Postcode Range

The batteries names, total watt capacity and average watt capacity can be retrieved by `GET` request to the `/batteries` endpoint of the server along with a filtered postcode range placed in the `postcodeStart` and `postcodeEnd` query parameters.

```sh
$curl -L -X GET 'localhost:8080/?postcodeStart=3433&postcodeEnd=6100'
```

This should return for the prior dataset

```json
{
    "names": [
        "Anker",
        "Energizer",
        "Aukey"
    ],
    "totalWattCapacity": 300,
    "averageWattCapacity": 100
}
```