#!/bin/sh

curl -s "https://raw.githubusercontent.com/nl2go/ansible-pull-init/master/ansible-install.sh" | sh

cat "* * * * * root curl -s {{hostHeartbeatUrl}}?hostToken={{hostToken}} | sh" > /etc/cron.d/infrastructr-deck-heartbeat
