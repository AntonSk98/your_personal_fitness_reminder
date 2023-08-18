#!/bin/bash

dir="C:/ansk/github/your_personal_fitness_reminder"
deploy_dir="${dir}/deploy"
target_dir="${dir}/target"
version="1.3"
jar_name="your_personal_fitness_reminder-${version}.jar"

target_remote_dir="/home/ec2-user/your_personal_fitness_reminder"

remote_vm="ec2-user@ec2-13-53-201-35.eu-north-1.compute.amazonaws.com"
private_key_file="${deploy_dir}/key.pem"

cd $dir
echo "Compiling the project into JAR"
mvn clean install
echo "JAR was assembled successfully"
cd $target_dir
mv $jar_name $deploy_dir
cd $deploy_dir
echo "Replaced jar files"

echo "Cleaning obsolete files located on the server..."
ssh -i $private_key_file $remote_vm "rm -rf $target_remote_dir"
echo "Creating necessary directories on the server..."
ssh -i $private_key_file $remote_vm "mkdir -p $target_remote_dir/exercises"

echo "Copying files to the remote server"
scp -i $private_key_file properties.yml $remote_vm:$target_remote_dir
scp -i $private_key_file docker-compose.yml $remote_vm:$target_remote_dir
scp -i $private_key_file $jar_name $remote_vm:$target_remote_dir
scp -i $private_key_file -r exercises $remote_vm:$target_remote_dir
echo "Successfully copied files to the remote server"

echo "Cleaning..."
rm $jar_name

echo "Trying to ssh into $remote_vm"
ssh -i $private_key_file $remote_vm << EOF
	echo "Successfully connected to the remote VM"
	cd $target_remote_dir
	echo -n "I am in: " && pwd
	if ! pgrep docker > /dev/null; then
	  echo "Docker daemon is not running. Restarting Docker..."
	    sudo systemctl enable docker
	    sudo systemctl start docker
	      if ! pgrep docker > /dev/null; then
	          echo "Failed to start Docker."
		      exit 1
		        fi
			  echo "Docker has been restarted successfully."
		  else
		    echo "Docker daemon is running."
	fi
	echo "Stopping existing cluster"
	docker-compose down
	echo "Building new cluster..."
	docker-compose up -d
	exit
EOF
echo "Successfully redeployed the cluster!"
