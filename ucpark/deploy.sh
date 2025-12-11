#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

echo "Building project..."
mvn clean package

echo "Deploying to GlassFish..."
# --force=true allows redeploying even if the application already exists
# Delete existing resources first to ensure update (ignore errors if they don't exist)
asadmin delete-jdbc-resource jdbc/ucPark || true
asadmin add-resources ucpark_ear/glassfish-resources.xml

asadmin deploy --force=true ucpark_ear/target/ucpark_ear-1.0-SNAPSHOT.ear

echo "Deployment successful!"
