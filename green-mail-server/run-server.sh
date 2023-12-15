#!/bin/bash

# Default values
PORT=5001
SMTP_PORT=3025
IMAP_PORT=3143
HOSTNAME="0.0.0.0"

# Display variable values
echo "PORT: PORT"
echo "SMTP_PORT: $SMTP_PORT"
echo "IMAP_PORT: $IMAP_PORT"
echo "HOSTNAME: $HOSTNAME"

# Run the Java command
java -Dgreenmail.api.port="$PORT" -Dgreenmail.smtp.port="$SMTP_PORT" -Dgreenmail.imap.port="$IMAP_PORT" -Dgreenmail.hostname="$HOSTNAME" -jar greenmail-standalone-2.1.0-alpha-3.jar

