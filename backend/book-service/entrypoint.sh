#!/bin/bash

response=$(curl -s ${ECS_CONTAINER_METADATA_URI_V4})
export ECS_INSTANCE_IP_ADDRESS=$(echo $response | jq -r '.Networks[0].IPv4Addresses[0]')
echo $ECS_INSTANCE_IP_ADDRESS

java -jar book-service.jar