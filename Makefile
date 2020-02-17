%.class: %.java
	javac *.java

run: TestCreator.class
	java TestCreator

clean:
	rm *.class
