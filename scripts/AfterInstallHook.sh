#!/bin/bash
rm -rf /home/ec2-user/build/ROOT.war
mv /home/ec2-user/build/target/kgpproductone.war /home/ec2-user/build/ROOT.war
sudo cp /home/ec2-user/build/ROOT.war /opt/projectone/tomcat/webapps/