#!/bin/bash

SUCCESS=0
BLOCKED=0

for i in {1..20}; do
  CODE=$(curl -s -o /dev/null -w "%{http_code}" http://web.typ-app.local/)
  if [ "$CODE" = "409" ]; then
    BLOCKED=$((BLOCKED+1))
  else
    SUCCESS=$((SUCCESS+1))
  fi
done

echo "✅ Successful requests (HTTP 200): $SUCCESS"
echo "⛔ Blocked requests (rate-limited): $BLOCKED"
