# Scala-Seq-Renamer

App to copy/rename sequential animation files into freeze frames

## Prerequisites

- Java: openjdk version "11" (or similar)
- Maven: Apache Maven 3.8.3 (or similar)

## Build

```
mvn clean install
```

### Running IT tests

```
mvn test -P it-tests
```

## CLI Help page

```
java -jar target/scala-seq-renamer-1.0.0-SNAPSHOT.jar --help
```

# Usage example

```
java -jar target/scala-seq-renamer-1.0.0-SNAPSHOT.jar -i="C:/input/*.jpg" -o="C:/output/*.jpg" -p=":8"
```
