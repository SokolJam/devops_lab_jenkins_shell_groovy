#! /bin/bash

yum -y install net-tools vim java-1.8.0-openjdk-devel postgresql-server postgresql-contrib nginx unzip

useradd sonar

postgresql-setup initdb
systemctl enable postgresql
systemctl start postgresql

#create and configure DB
sudo -u postgres psql <<EOF
create user sonar;
alter role sonar with createdb;
alter user sonar with encrypted password 'sonar';
create database sonar owner sonar;
grant all privileges on database sonar to sonar;
EOF

sed -i 's/ident/md5/' /var/lib/pgsql/data/pg_hba.conf
systemctl restart postgresql

#download and configure Sonar
wget https://sonarsource.bintray.com/Distribution/sonarqube/sonarqube-7.2.zip
unzip sonarqube-7.2.zip
mkdir /opt/sonar/
cp -r sonarqube-7.2/* /opt/sonar/
chown sonar:sonar -R /opt/sonar/

cat > /opt/sonar/conf/sonar.properties <<EOF
sonar.jdbc.username=sonar
sonar.jdbc.password=sonar
sonar.jdbc.url=jdbc:postgresql://localhost/sonar
sonar.web.port=9000
EOF

#configure systemd scripr for Sonar
cat > /etc/systemd/system/sonar.service <<EOF
[Unit]
Description=Sonar
After=network.target network-online.target
Wants=network-online.target

[Service]
ExecStart=/opt/sonar/bin/linux-x86-64/sonar.sh start
ExecStop=/opt/sonar/bin/linux-x86-64/sonar.sh stop
PIDFile=/opt/sonar/bin/linux-x86-64/./SonarQube.pid
ExecReload=/opt/sonar/bin/linux-x86-64/sonar.sh restart
Type=forking
User=sonar

[Install]
WantedBy=multi-user.target
EOF

# start a jenkins service
systemctl daemon-reload
systemctl enable sonar
systemctl start sonar

#configure Nginx server for navigation to Jenkins without port
cat  > /etc/nginx/conf.d/sonar.conf <<EOF
upstream sonar {
                server 127.0.0.1:9000;
                }

server {
    listen      80;
    server_name sonar;

    access_log  /opt/sonar/logs/sonar_access.log;
    error_log   /opt/sonar/logs/sonar_error.log;

    location / {
        proxy_pass  http://sonar;
        proxy_redirect off;
        }
}
EOF

sed -i 's/default_server//' /etc/nginx/nginx.conf

# start nginx service
systemctl enable nginx
systemctl start nginx

