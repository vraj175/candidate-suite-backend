#!/bin/bash
rm -rf /home/ec2-user/build/ROOT.war
mv /home/ec2-user/build/target/candidate-suite-backend-0.0.1-SNAPSHOT.war /home/ec2-user/build/ROOT.war
sudo cp /home/ec2-user/build/ROOT.war /opt/candidatesuite/tomcat/webapps/