for i in {1..1000}; do
    curl -X POST -H "Content-Type: text/plain" -d "Hello Kafka!" http://localhost:8080/api/kafka/publish
done