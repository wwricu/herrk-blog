#!/bin/bash
sudo rm ./logs/*
sudo docker stop server
sudo docker rm server
sudo `pwd`/myserver.sh
