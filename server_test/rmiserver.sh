#!/bin/bash

export SECPOLICY="file:./policy"
java -cp . -Djava.security.policy=$SECPOLICY Server
