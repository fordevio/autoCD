build: get
	@npm --prefix ./src/client run build  && mvn -f ./src/producer/pom.xml clean install -DskipTests -Dmaven.test.skip=true

run:
	@mvn -f ./src/producer/pom.xml spring-boot:run

get:
	@npm --prefix ./src/client install

test:
	@mvn -f ./src/producer/pom.xml test

format:
	@npm --prefix ./src/client run format
	
runReact: 
	@npm --prefix ./src/client start

buildProducerDevImage:
	@docker build -t autocd-producer -f ./src/producer/Dockerfile.dev ./src/producer/

buildClientDevImage: get
	@docker build -t autocd-client -f ./src/client/Dockerfile.dev ./src/client/

runProducerDevContainer:
	@docker run -it --rm -v $(shell pwd)/src/producer/:/app -v /var/autocd:/var/autocd -p 5001:5001 autocd-producer /bin/bash -c "mvn spring-boot:run" 

runClientDevImage:
	@docker run -it --rm -v $(shell pwd)/src/client/:/app -p 3000:3000 autocd-client /bin/bash -c "npm run start" 


help:
	@echo "Available commands:"
	@echo "  make build                  - Build the application"
	@echo "  make buildProducerDevImage   - Build java dev docker image"
	@echo "  make runProducerDevContainer - Run java dev docker image"
	@echo "  make buildClientDevImage     - Build client dev image"
	@echo "  make runClientDevImage"      - Run client dev image"
	@echo "  make run                     - Run the application"
	@echo "  make runReact                - Run the frontend on port 3000"
	@echo "  make get                     - Install dependencies"
	@echo "  make format                  - Format code"
	@echo "  make test                    - Run all test"
	@echo "  make help                    - Show this help message"