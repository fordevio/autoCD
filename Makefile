build: 
	@npm --prefix ./src/client run build 

run:
	@mvn -f ./src/producer/pom.xml spring-boot:run

runReact: 
	@npm --prefix ./src/client start

buildProducerDevImage:
	@docker build -t autocd-producer -f ./src/producer/Dockerfile.dev ./src/producer/

runProducerDevContainer:
	@docker run -it --rm -v $(shell pwd)/src/producer/:/app -v /var/autocd:/var/autocd -p 5001:5001 autocd-producer /bin/bash -c "mvn spring-boot:run" 