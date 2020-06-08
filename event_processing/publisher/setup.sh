#!/bin/bash

sleep 200
curl -X POST "http://web-server:8080/junctions/" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"id\": 3, \"latitude\": 55.70789, \"longitude\": 37.83499, \"name\": \"МКАД - Рязанский пр.\", \"radius\": 1000.0}"
sleep 3
