#/bin/bash

# Display variable values
# export SONAR_TOKEN=your_actual_token_value
echo "SONAR_TOKEN: $SONAR_TOKEN"

mvn clean compile verify sonar:sonar -Dsonar.token="$SONAR_TOKEN"
