# MiniJava-Compiler

This project is a MiniJava Compiler written in Java that produces code in the [LLVM instruction set](https://llvm.org/docs/LangRef.html#instruction-reference). The [MiniJava language](http://www.cambridge.org/resources/052182060X/) is a subset of the Java language. A script is provided which compiles the LLVM instruction set files to executable programs (with the help of Clang).

## Getting Started

The following steps are required in order to set up your enviroment and use the compiler.

### Prerequisites

At first you will need to install the Java Runtime Environment (JRE) and the Java Development Kit (JDK)
```
sudo apt-get install default-jre
sudo apt-get install default-jdk
```
Then you will need to install the Clang 4.0
```
sudo apt-get install clang-4.0
```

### Installing

1) Get a copy of the project
```
git clone https://github.com/v-pap/MiniJava-Compiler.git
```
2) Go into the src directory
```
cd MiniJava-Compiler/src
```
3) Compile the project
```
make
```
## Using the Compiler

In order to compile a MiniJava program you will have to type

```
java Main file.java
```
If you have multiple MiniJava programs to run you can add them as extra arguments.
For example if you have 2 MiniJava programs you will run them this way

```
java Main file1.java file2.java
```
The compiler will produce an LLVM file for each MiniJava program.

In the first example it will produce the
```
file.ll
```
and on the second example it will produce
```
file1.ll file2.ll
```

## Compiling the LLVM instruction set to executable code 

A script is provided in the scripts directory which when run on the same directory as the LLVM files (.ll) will produce executable code.

### Running the script

Fixing the script permissions (only one time needed)
```
chmod +x compile_script.sh
```
Running the script
```
./compile_script.sh
```
Note: You may need to copy the script in the directory in which the LLVM files are located.

## License

This project is licensed under the MIT License
