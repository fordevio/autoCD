build: installDependencies
	@npm --prefix ./src/client run build  && mvn -f ./src/producer/pom.xml clean install -DskipTests -Dmaven.test.skip=true

run:
	@mvn -f ./src/producer/pom.xml spring-boot:run

installDependencies:
	@npm --prefix ./src/client install

test:
	@mvn -f ./src/producer/pom.xml test

runReact: 
	@npm --prefix ./src/client start

buildProducerDevImage:
	@docker build -t autocd-producer -f ./src/producer/Dockerfile.dev ./src/producer/

runProducerDevContainer:
	@docker run -it --rm -v $(shell pwd)/src/producer/:/app -v /var/autocd:/var/autocd -p 5001:5001 autocd-producer /bin/bash -c "mvn spring-boot:run" 