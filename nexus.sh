#! /bin/bash

nexus_path=/opt/nexus
yum -y install net-tools vim java-1.8.0-openjdk-devel nginx 

useradd nexus
wget http://download.sonatype.com/nexus/3/latest-unix.tar.gz

mkdir $nexus_path/
tar -xvzf latest-unix.tar.gz -C $nexus_path/
chown -R nexus:nexus $nexus_path

#configure systemd scripr for Nexus
cat > /etc/systemd/system/nexus.service <<EOF
Unit]
Description=nexus service
After=network.target
  
[Service]
Type=forking
ExecStart=$nexus_path/nexus-3.13.0-01/bin/nexus start
ExecStop=$nexus_path/nexus-3.13.0-01/bin/nexus stop
User=nexus
Restart=on-abort
  
[Install]
WantedBy=multi-user.target
EOF

# start a jenkins service
systemctl daemon-reload
systemctl enable nexus
systemctl start nexus

#configure Nginx server for navigation to Jenkins without port
cat  > /etc/nginx/conf.d/nexus.conf <<EOF
upstream nexus {
                server 127.0.0.1:8081;
                }

server {
    listen      80;
    server_name nexus;

    client_max_body_size 1G;

    access_log  /opt/nexus/sonatype-work/nexus3/log/nexus_access.log;
    error_log   /opt/nexus/sonatype-work/nexus3/log/nexus_error.log;

    location / {
        proxy_pass  http://nexus;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
}
EOF

sed -i 's/default_server//' /etc/nginx/nginx.conf

# start nginx service
systemctl enable nginx
systemctl start nginx

