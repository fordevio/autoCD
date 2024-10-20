buildDevImage:
	@docker build -t autocd-producer -f Dockerfile.dev .

runDevContainer:
	@docker run -it --rm -v $PWD:/app -v /var/autocd:/var/autocd -p 5001:5001 autocd-producer /bin/bash

runMvn:
	@mvn spring-boot:run
