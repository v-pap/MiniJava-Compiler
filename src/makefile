all: compile

compile:
	java -jar ../jar/jtb132di.jar -te ../grammar/minijava.jj
	java -jar ../jar/javacc5.jar ../grammar/minijava-jtb.jj
	javac Main.java

clean:
	rm -f *.class *~
	rm -rf syntaxtree visitor
