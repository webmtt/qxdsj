#!/bin/bash

export MAVEN_HOME=/export/workspace/apache-maven-3.5.4
cd  ../kettle-scheduler-backstage
mvn clean install -Dmaven.test.skip=true
