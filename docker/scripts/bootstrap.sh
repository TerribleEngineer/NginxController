#!/bin/bash
wget -O init-api.js http://127.0.0.1:7777/nginx/register/api
curl -H "Content-Type: application/json" -X POST  -d @init-api.js http://127.0.0.1:7777/nginx/config

wget -O init-ui.js http://127.0.0.1:7777/nginx/register/ui
curl -H "Content-Type: application/json" -X POST  -d @init-ui.js http://127.0.0.1:7777/nginx/config
