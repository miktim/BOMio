#!/bin/bash

echo $(javac -version)
echo $(java -version)
if [ -f ../../dist/java-bomio-2.0.0.jar ]; then
  javac -cp ../../dist/java-bomio-2.0.0.jar BomIoTest.java
  java  -cp ../../dist/java-bomio-2.0.0.jar:. BomIoTest
  rm -f *.class
else
  echo First make the java-bomio-2.0.0.jar file.
fi
echo
echo Completed. Press Enter to exit...
read
