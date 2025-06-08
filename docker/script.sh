#!/bin/bash

# === CONFIGURATION ===
CLIENT_ID="7199598147-77mkhqmehf4hiq0ltug4eofm1308anfc.apps.googleusercontent.com"  # <-- Replace this with your actual Google OAuth client ID
CLIENT_SECRET="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"
SCOPE="openid email profile"
API_ENDPOINT="http://localhost:8080/api/v1/accounts/create"

# === STEP 1: Get device code from Google ===
echo "Requesting device code..."
RESPONSE=$(curl -s -X POST https://oauth2.googleapis.com/device/code \
  -d client_id=$CLIENT_ID \
  -d scope="$SCOPE")

DEVICE_CODE=$(echo $RESPONSE | jq -r '.device_code')
USER_CODE=$(echo $RESPONSE | jq -r '.user_code')
VERIFICATION_URL=$(echo $RESPONSE | jq -r '.verification_url')
INTERVAL=$(echo $RESPONSE | jq -r '.interval')

if [[ -z "$DEVICE_CODE" || "$DEVICE_CODE" == "null" ]]; then
  echo "Error: Failed to retrieve device code. Response:"
  echo $RESPONSE
  exit 1
fi

echo -e "\nPlease go to the following URL and enter the code:"
echo -e "\033[1;32m$VERIFICATION_URL\033[0m"
echo -e "User Code: \033[1;34m$USER_CODE\033[0m\n"

# === STEP 2: Poll for token ===
echo "Waiting for user to authorize the app..."
read -p ">>"

TOKEN_RESPONSE=$(curl -s -X POST https://oauth2.googleapis.com/token \
  -d client_id=$CLIENT_ID \
  -d device_code=$DEVICE_CODE \
  -d client_secret=$CLIENT_SECRET \
  -d grant_type=urn:ietf:params:oauth:grant-type:device_code)

ID_TOKEN=$(echo $TOKEN_RESPONSE | jq -r '.id_token')

if [[ "$ID_TOKEN" != "null" ]]; then
  echo -e "\nID token acquired."
else
  echo "Error: ID token not received. Response:"
  echo $TOKEN_RESPONSE
  exit 1
fi

echo "ID token is: $ID_TOKEN"

# === STEP 3: Call your local secured Spring Boot API ===
echo -e "\nCalling your Spring Boot API using the token..."

curl -i -X POST "$API_ENDPOINT" \
  -H "Authorization: Bearer $ID_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
        "accountNumber": "DE89370400440532013000",
        "balance": 1500.00,
        "currency": "GBP",
        "registered": "2025-04-09",
        "owner": "Jane Doe",
        "accountType": "COMMERCIAL"
      }'