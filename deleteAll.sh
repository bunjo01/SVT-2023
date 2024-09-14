#!/bin/bash

# Get all index names
indices=$(curl -X GET "http://localhost:9200/_cat/indices?h=index" -s)

# Delete each index
for index in $indices
do
  curl -X DELETE "http://localhost:9200/$index"
done
