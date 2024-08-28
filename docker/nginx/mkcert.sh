#! /usr/bin/env bash

openssl req -new -newkey rsa:2048 -nodes -keyout todo-api.local.key -out todo-api.local.csr
openssl x509 -req -days 365 -in todo-api.local.csr -signkey todo-api.local.key -out todo-api.local.crt
