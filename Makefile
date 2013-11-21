all:
	javac -Xlint src/*.java -d class/
compile:
	javac src/*.java -d class/
run:
	appletviewer -J"-Djava.security.policy=security.policy" Blackjack.html
clean:
	rm -rf class/*.class
