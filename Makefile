runKafka:
	@docker run --rm -d -p 9092:9092 --name broker apache/kafka:latest


