#!/bin/bash
COUNT=1500
STEP=100
CURRENT=$COUNT
while true; do
	node commit_passwords.js fill $CURRENT
	let CURRENT=$CURRENT+$STEP
done