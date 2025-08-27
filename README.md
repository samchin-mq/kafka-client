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
tar -xzf kafka_2.13-3.6.1.tgz
echo security.protocol=PLAINTEXT > client.properties

export KAFKA_ROOT=$(pwd)/kafka_2.13-3.6.1
export BOOTSTRAP_SERVER="b-1.sammsk3.0q3vg2.c4.kafka.ap-northeast-1.amazonaws.com:9092,b-2.sammsk3.0q3vg2.c4.kafka.ap-northeast-1.amazonaws.com:9092,b-3.sammsk3.0q3vg2.c4.kafka.ap-northeast-1.amazonaws.com:9092"
$KAFKA_ROOT/bin/kafka-topics.sh --list --bootstrap-server $BOOTSTRAP_SERVER
sudo yum install -y java-21-amazon-corretto-devel
sudo update-alternatives --config java
wget https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.tar.gz
sudo tar xvf apache-maven-3.9.11-bin.tar.gz -C /opt
sudo ln -s /opt/apache-maven-3.9.11 /opt/maven
export M2_HOME=/opt/maven
export PATH=${M2_HOME}/bin:${PATH}
sudo yum install -y maven

mvn spring-boot:run
java -classpath ./target/classes org.example.kafka.util.TestHttpClientString

cd /home/ec2-user/environment/kafka-client; /opt/c9/dependencies/redhat.java@linux-x64/3766f5fd94863af1f93a836b48f61b86550fca34421e1cb7c02cc2994853a1b56bdc5769dfcbb19c20591ed10c7b96e8a98fb5fa30ca65678cb6a415ac3c3f13/jre/17.0.3-linux-x86_64/bin/java -XX:+ShowCodeDetailsInExceptionMessages @/tmp/cp_5h95jyu20on1jyg587urd9cmr.argfile org.example.kafka.util.TestHttpClientString

git config --global user.name "sam.chin"
git config --global user.email sam.chin@merquri.io

wget https://github.com/aws/aws-msk-iam-auth/releases/download/v2.3.2/aws-msk-iam-auth-2.3.2-all.jar
export BOOTSTRAP_SERVER=b-3.sam3broker.m24vht.c4.kafka.ap-southeast-1.amazonaws.com:9092,b-2.sam3broker.m24vht.c4.kafka.ap-southeast-1.amazonaws.com:9092,b-1.sam3broker.m24vht.c4.kafka.ap-southeast-1.amazonaws.com:9092
export BOOTSTRAP_SERVER=boot-1mn.samekafka.1uq8qg.c4.kafka.ap-southeast-1.amazonaws.com:9092,boot-a6k.samekafka.1uq8qg.c4.kafka.ap-southeast-1.amazonaws.com:9092,boot-x1r.samekafka.1uq8qg.c4.kafka.ap-southeast-1.amazonaws.com:9092
export BOOTSTRAP_SERVER=b-2.sam2zonemsk.iss3jo.c4.kafka.ap-southeast-1.amazonaws.com:9092,b-1.sam2zonemsk.iss3jo.c4.kafka.ap-southeast-1.amazonaws.com:9092,b-3.sam2zonemsk.iss3jo.c4.kafka.ap-southeast-1.amazonaws.com:9092
$KAFKA_ROOT/bin/kafka-topics.sh --bootstrap-server $BOOTSTRAP_SERVER --list --command-config client.properties
$KAFKA_ROOT/bin/kafka-topics.sh --bootstrap-server $BOOTSTRAP_SERVER --create --topic stringtopicnew2
$KAFKA_ROOT/bin/kafka-topics.sh --bootstrap-server $BOOTSTRAP_SERVER --alter --topic stringtopic1 --partitions 12
$KAFKA_ROOT/bin/kafka-topics.sh --bootstrap-server $BOOTSTRAP_SERVER --describe --topic stringtopic --command-config client.properties
$KAFKA_ROOT/bin/kafka-reassign-partitions.sh --bootstrap-server $BOOTSTRAP_SERVER --generate --topics-to-move-json-file move-topic.json --broker-list "1,2,3,4,5,6"
$KAFKA_ROOT/bin/kafka-reassign-partitions.sh --bootstrap-server $BOOTSTRAP_SERVER --execute --reassignment-json-file new-stringtopic.json