# SignalJ
A compiler of SignalJ programming language.

This comiler is developped on the basis of ExtendJ, an extensible compiler of Java. http://jastadd.org/web/extendj/
All tools needed are included in ExtendJ.  To build our compiler, you only need to have javac and Apache Ant.

To build the SignalJ compiler, you need to follow the following instructions.

1. Download and extract ExtendJ compiler.
2. Copy the directory named "signalj" to the top-level dierctory of the extracted ExtendJ directory.
3. Change directory to the copied signalj directory.
4. Run "ant signalj jar".
Then, the jar file "signalj.jar" will be created in the directory.

To run the compiler, you just need to run the following command.
$ java -jar signalj.jar [source files]
