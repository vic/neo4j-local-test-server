A single jar neo4j server using an impermanent store.

This server is intended for unit testing an application that relies on neo4j graphs,
mainly on non-java environments. In java, you can always create a test neo4j server
in the same process, but other languages require you to actually access neo4j via
its REST interface. This project simply wraps the impermanent store on a REST server.

So your tests will still perform REST requests to the server but it will be a lot
faster since it wont be storing anything to disk.

Please use this only for testing applications.


```shell
mvn clean package
java -jar target/neo4j-local-test-server-1.0-SNAPSHOT.one-jar.jar
```


Testing your application would require to execute the server once before all tests are
run and then, kill the server when the test process has finished.
