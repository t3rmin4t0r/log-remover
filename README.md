log-remover
===========

This is a java agent that pre-processes byte-code before it is loaded by a class loader.

This goes through all the org.apache.hadoop.\* classes and removes calls to Log.debug() from them.

To test it, we benchmark 4 million calls to Log.debug()

	$ java -javaagent:target/log-remover-1.0-SNAPSHOT.jar -jar target/log-remover-1.0-SNAPSHOT.jar
	We took 1 ms to do 4 M loops

and without the agent
	$ java  -jar target/log-remover-1.0-SNAPSHOT.jar 
	We took 33 ms to do 4 M loops
