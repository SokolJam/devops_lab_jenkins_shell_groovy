# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  config.vm.box = "sbeliakou/centos"
  config.vm.box_version = "7.5"
  
  config.vm.define "jenkins_serv" do |jenkins|
    jenkins.vm.provider :"virtualbox" do |virt|
      virt.name = "jenkins"
      virt.memory = "4096"
    end
    jenkins.vm.hostname = "jenkins"
    jenkins.vm.network "private_network", ip: "192.168.100.2"
    jenkins.vm.provision :shell, path: "provision.sh"
    jenkins.vm.synced_folder "/home/student/Jenkins/jenkins/", "/opt/jenkins"
  end
  
  (1..2).each do |i|
     config.vm.define "node#{i}" do |node|
      node.vm.provider "virtualbox"  do |slave|
       slave.name = "node#{i}"
      end
      node.vm.hostname = "node#{i}"
      node.vm.network "private_network", ip: "192.168.100.#{i+2}"
      node.vm.provision "shell", inline: <<-SHELL
	yum -y install mc net-tools vim java-1.8.0-openjdk-devel    
      SHELL 
     end
    end 

  config.vm.define "sonar_serv" do |sonar|
    sonar.vm.provider :"virtualbox" do |serv|
      serv.name = "sonar"
      serv.memory = "2048"
    end
    sonar.vm.hostname = "sonar"
    sonar.vm.network "private_network", ip: "192.168.100.5"
    sonar.vm.provision :shell, path: "sonar.sh"
  end
end
