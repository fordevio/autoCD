buildDevImage:
	@docker build -t autocd-producer -f Dockerfile.dev .

runBackendDevContainer:
	@docker run -it --rm -e KAFKA_HOST="localhost:9092" -v $(shell pwd):/app -v /var/autocd:/var/autocd -p 5001:5001 autocd-producer /bin/bash -c "mvn spring-boot:run" 

runApp:
	@mvn spring-boot:run
