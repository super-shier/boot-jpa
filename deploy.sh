mvn clean package -Dmaven.test.skip=true
scp -r target/jpa.jar jpa@47.98.178.129:/home/jpa/jpa/builds