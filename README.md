# scala-grpc-test

[![Build Status](https://travis-ci.org/nokamoto/scala-grpc-test.svg?branch=master)](https://travis-ci.org/nokamoto/scala-grpc-test)

```
sbt -Dport=${port} server/run
sbt -Dhost=${host} -Dport=${port} client/run
```

```
sbt server/assembly client/assembly
java -Dport=${port} -jar server/target/scala-2.11/scala-grpc-test-server.jar
java -Dhost=${host} -Dport=${port} -jar client/target/scala-2.11/scala-grpc-test-client.jar
```
