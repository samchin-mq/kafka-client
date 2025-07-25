         ___        ______     ____ _                 _  ___  
        / \ \      / / ___|   / ___| | ___  _   _  __| |/ _ \ 
       / _ \ \ /\ / /\___ \  | |   | |/ _ \| | | |/ _` | (_) |
      / ___ \ V  V /  ___) | | |___| | (_) | |_| | (_| |\__, |
     /_/   \_\_/\_/  |____/   \____|_|\___/ \__,_|\__,_|  /_/ 
 ----------------------------------------------------------------- 


Hi there! Welcome to AWS Cloud9!

To get started, create some files, play with the terminal,
or visit https://docs.aws.amazon.com/console/cloud9/ for our documentation.

Happy coding!

# Example: Download Kafka 3.6.1 (adjust version as needed)
wget https://archive.apache.org/dist/kafka/3.6.1/kafka_2.13-3.6.1.tgz
export KAFKA_ROOT=$(pwd)/kafka_2.13-3.6.1
tar -xzf kafka_2.13-3.6.1.tgz
export BOOTSTRAP_SERVER="b-1.your-cluster.xyz.kafka.your-region.amazonaws.com:9092,b-2.your-cluster.abc.kafka.your-region.amazonaws.com:9092" # Replace with your actual bootstrap brokers
$KAFKA_ROOT/bin/kafka-topics.sh --list --bootstrap-server $BOOTSTRAP_SERVER --command-config client.properties
sudo yum install -y java-21-amazon-corretto-devel
sudo yum install -y maven
mvn spring-boot:run