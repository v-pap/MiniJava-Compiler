FROM ubuntu

# Install jre, jdk and clang
RUN apt-get update && apt-get -y install git default-jre default-jdk clang-4.0 build-essential
RUN git clone https://github.com/v-pap/MiniJava-Compiler.git
WORKDIR /MiniJava-Compiler/src
RUN make
RUN echo 'alias compile="java -cp /MiniJava-Compiler/src: Main"' >> ~/.bashrc
WORKDIR /~
