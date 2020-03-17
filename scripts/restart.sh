#!/bin/bash

sbt clean stage
sudo supervisorctl restart covid-certificate-generator