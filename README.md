# vocalink-stream-processor

A stream processor is a program which takes as input one or more streams of events/messages, runs some
computation for each event, and generates output in the form of other events, side effects, or a combination
of the two.
In this exercise you will write a streaming service which processes ```Transaction``` messages
asynchronously and generates ```TransactionResult``` messages.
The action performed for each ```Transaction``` message is defined in the following interface

```java
public interface Service {
    CompletionStage<Optional<String>> processTransaction(Transaction tx);
}
```

The resulting TransactionResult depends on the returned value of processTransaction() :
* Successful future holding ```Optional.empty()``` if the operation completed successfully
* Successful future holding ```Optional.of(errorMsg)``` if the operation failed because of a business
error detailed in the errorMsg string
* Failed future if the ```CompletionStage``` completes exceptionally
Note that under any of the above scenarios the stream processor should guarantee that a
```TransactionResult``` message is generated for each ```Transaction```.

Assumptions
---
You can define what ```Transaction``` and ```TransactionResult``` look like, it should however be
possible to distinguish a successful result from an error one, in which case the failure cause should be
present in the message. Also, the processTransaction method can be implemented as you prefer for
the purpose of running/testing the service, but the signature should preferably not be altered.
You can use your favourite form of input (i.e. from file, or generated at runtime) to feed the processor, as
long as the input data is streamed and not preemptively loaded in memory. The same goes for the
generated messages which can be printed to stdout for simplicity.

Tests and technologies
---
All the code should be written in Java 8+ with no restriction to the libraries and framework adopted.
However, we encourage you to use Akka-streams as it's one of the main frameworks we rely on.

The code should be well tested using either Java or Kotlin as programming language.

Implementation
---
for the scope of this exercise a simple logic has been implemented for processTransaction method in the main service:
* a transaction is represented by a simple string read from input stream;
* TransactionResult is an object containing the error message if present and the status: _SUCCESS_, _FAILED_ and _ERROR_;
* if the transaction contains the word _vocalink_ everywhere in the test then the transaction is successful: status = _SUCCESS_;
* if the transaction string contains more then 30 characters, an exception is thrown so we can easily test scenario 3 of the exercise. The ```TransactionResult``` would contain both error message and the status _FAILED_;
* returns _ERROR_ with the error message in every other case;  

The stream it's a simple Akka stream ```Source``` from Standard input, with different transformation:
* convert the input into a ```Transaction``` object with the string message;
* process the transaction using the signature specified in the exercise;
* get the result out of the ```CompetableFutrure``` object;
* map the result to ```TransactionResult```;
* logging the ```TransactionResult``` to Standard output;

_NOTE:_ 

1. ```processTransaction()``` could have return straight a ```TransactionResult``` but in this case, the signature of the exercise has been respected.
2. error handling are configured only on ```processTransaction()``` method, other step have no error handling specified;
3. No other frameworks/libraries such as Spring or similar have been used apart from Akka-streams, slf4j-log4j12 and Lombok to make code concise.

Requirements:
---
* java 11

Run:
---
Build the jar as following using embedded gradle wrapper:
```bash
./gradlew clean build shadowJar 
```

run the jar as following:
```bash
java -jar build/libs/vocalink-stream-processor-1.0-all.jar
```

You can also run as following:
```bash 
./gradlew clean build shadowJar run
```
so the java process would be handled by Gradle and so the console output looks slower.

type some string on the standard input (console prompt) and press 'ENTER' to send the message to the Stream.
TransactionResult are logged to the console.