#!/bin/bash

echo "========================================="
echo "AUTH SERVICE TEST SUITE"
echo "========================================="
echo ""

BASE_URL="http://localhost:18081"

echo "1️⃣  Testing Service Health..."
curl -s "$BASE_URL/api/auth/oauth-info" | jq . || echo "❌ Service not ready"
echo ""

echo "2️⃣  Testing User Registration..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "student1",
    "email": "student1@test.com",
    "password": "Test@123",
    "fullName": "Test Student"
  }')

echo "$REGISTER_RESPONSE" | jq .
USER_ID=$(echo "$REGISTER_RESPONSE" | jq -r '.id // empty')

if [ -n "$USER_ID" ]; then
  echo "✅ Registration successful! User ID: $USER_ID"
else
  echo "❌ Registration failed"
fi
echo ""

echo "3️⃣  Testing OAuth2 Login (Password Grant)..."
TOKEN_RESPONSE=$(curl -s -X POST "$BASE_URL/oauth2/token" \
  -u "auth-client:secret" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&username=student1&password=Test@123")

echo "$TOKEN_RESPONSE" | jq .
ACCESS_TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.access_token // empty')
REFRESH_TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.refresh_token // empty')

if [ -n "$ACCESS_TOKEN" ]; then
  echo "✅ Login successful!"
  echo "   Access Token: ${ACCESS_TOKEN:0:50}..."
else
  echo "❌ Login failed"
fi
echo ""

echo "4️⃣  Testing Token Refresh..."
if [ -n "$REFRESH_TOKEN" ]; then
  REFRESH_RESPONSE=$(curl -s -X POST "$BASE_URL/oauth2/token" \
    -u "auth-client:secret" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "grant_type=refresh_token&refresh_token=$REFRESH_TOKEN")
  
  echo "$REFRESH_RESPONSE" | jq .
  NEW_ACCESS_TOKEN=$(echo "$REFRESH_RESPONSE" | jq -r '.access_token // empty')
  
  if [ -n "$NEW_ACCESS_TOKEN" ]; then
    echo "✅ Token refresh successful!"
  else
    echo "❌ Token refresh failed"
  fi
else
  echo "⏭️  Skipped (no refresh token)"
fi
echo ""

echo "5️⃣  Testing Duplicate Registration..."
DUPLICATE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "student1",
    "email": "student1@test.com",
    "password": "Test@123",
    "fullName": "Test Student"
  }')

echo "$DUPLICATE_RESPONSE" | jq .
if echo "$DUPLICATE_RESPONSE" | grep -q "already exists"; then
  echo "✅ Duplicate validation working!"
else
  echo "⚠️  Duplicate registration check may have issues"
fi
echo ""

echo "========================================="
echo "TEST SUMMARY"
echo "========================================="
echo "Service URL: $BASE_URL"
echo "OAuth2 Token Endpoint: $BASE_URL/oauth2/token"
echo "Register Endpoint: $BASE_URL/api/auth/register"
echo "========================================="
