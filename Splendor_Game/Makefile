JCC = javac
JFLAGS = -d
SRC=$(wildcard ./src/*.java)
SRCDIR=./src/
BIN=./bin/
OBJECTS := $(SRC:$(SRCDIR)%.java=$(BIN)%.class)
OBJECTSTMP := $(SRC:$(SRCDIR)%.java=%.class)
EXEC=Main
NAMEPROJECT=Splendor

all: $(OBJECTS)

$(BIN)%.class : $(SRCDIR)%.java
	$(JCC) -sourcepath $(SRCDIR) -implicit:class $(JFLAGS)  $(BIN) $< 


execute: $(BIN)$(EXEC).class
	java -cp $(BIN) $(EXEC)


clean:
	rm -f ./bin/*.class core
	rm -fr ./docs/javadoc/*
	
mrproper: clean
	rm -f *.jar


createjar:
	cd bin ;\
	jar cfm $(NAMEPROJECT).jar MANIFEST.MF *.class
	cd ..;\
	pwd
	mv $(BIN)$(NAMEPROJECT).jar . 

executejar: $(NAMEPROJECT).jar
	java -jar $(NAMEPROJECT).jar 

javadoc: 
	javadoc -d ./docs/javadoc/ ./src/*.java

help:
	@echo "[MakeFile] > help : print help for user."
	@echo "[MakeFile] > clean : clean all class files ."
	@echo "[MakeFile] > execute : execute Main.java from compiled files."
	@echo "[MakeFile] > mrproper : clean all class / jar files ."
