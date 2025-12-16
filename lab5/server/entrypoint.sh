#!/bin/sh
set -e

echo "Starting server..."
python3 main.py &
SERVER_PID=$!

FAILS=0
MAX_FAILS=3

sleep 5
while true; do
	if curl -sf --max-time 2 http://127.0.0.1:8000/health > /dev/null; then
		FAILS=0
	else
		FAILS=$((FAILS + 1))
		echo "Healthcheck failed ($FAILS/$MAX_FAILS)"
	fi
	
	if [ "$FAILS" -ge "$MAX_FAILS" ]; then
		echo "Server is dead. Exiting container."
		kill $SERVER_PID
		exit 1
	fi

	sleep 3
done
