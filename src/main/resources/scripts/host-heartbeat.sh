#!/bin/sh

ansible -m setup localhost --tree /tmp/infrastructr-deck-facts >/dev/null 2>&1

curl -X POST -H "Content-Type: application/json" -d @/tmp/infrastructr-deck-facts/localhost "{{hostHeartbeatUrl}}?hostToken={{hostToken}}"
