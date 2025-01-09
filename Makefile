build: buildFrontend buildProducerDevImage
	@docker run -it --rm -v $(shell pwd)/src/producer/:/app -v /var/autocd:/var/autocd -p 5001:5001 autocd-producer /bin/bash -c "mvn clean install -DskipTests -Dmaven.test.skip=true" 

buildFrontend:
	@docker run -it --rm -v $(shell pwd)/:/app -p 3000:3000 autocd-client /bin/bash -c "npm --prefix ./src/client install && npm --prefix ./src/client run build"


test:
	@mvn -f ./src/producer/pom.xml test

format: buildClientDevImage
	@docker run -it --rm -v $(shell pwd)/src/client/:/app -p 3000:3000 autocd-client /bin/bash -c "npm install && npm run format" 
	

buildProducerDevImage:
	@docker build -t autocd-producer -f ./src/producer/Dockerfile.dev ./src/producer/

buildClientDevImage: 
	@docker build -t autocd-client -f ./src/client/Dockerfile.dev ./src/client/

runProducerDevContainer: buildProducerDevImage
	@docker run -it --rm -v $(shell pwd)/src/producer/:/app -v /var/autocd:/var/autocd -p 5001:5001 autocd-producer /bin/bash -c "mvn spring-boot:run" 

runClientDevContainer: buildClientDevImage
	@docker run -it --rm --network host -v $(shell pwd)/src/client/:/app autocd-client /bin/bash -c "npm run start" 


help:
	@echo "Available commands:"
	@echo "  make build                   - Build autoCD"
	@echo "  make buildFrontend           - Build the frontend"
	@echo "  make buildProducerDevImage   - Build java dev docker image"
	@echo "  make runProducerDevContainer - Run java dev docker image"
	@echo "  make buildClientDevImage     - Build client dev image"
	@echo "  make runClientDevContainer"      - Run client dev image"
	@echo "  make run                     - Run the application"
	@echo "  make get                     - Install dependencies"
	@echo "  make format                  - Format code"
	@echo "  make test                    - Run all test"
	@echo "  make help                    - Show this help message"