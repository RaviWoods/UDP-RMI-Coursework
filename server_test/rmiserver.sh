#!/bin/bash

export SECPOLICY="file:./policy"
java -cp . -Djava.security.policy=$SECPOLICY -Djava.rmi.server.hostname=146.169.21.39 Server

