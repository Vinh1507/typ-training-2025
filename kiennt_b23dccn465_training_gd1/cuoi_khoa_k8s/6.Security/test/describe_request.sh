#!/bin/bash
for i in {1..20}; do
  echo -n "Request $i → "
  curl -s -o /dev/null -w "%{http_code}\n" http://api.typ-app.local/actuator/health
done
