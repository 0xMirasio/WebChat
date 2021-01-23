# Webchat

Webchat is a java project for communication in a private network or internet + public server relay. 

### Installation
Webchat is using [Java 11 JDK](https://www.oracle.com/fr/java/technologies/javase-jdk11-downloads.html), we recommend you using this version to prevent any bug in execution.

Webchat is using [Maven](https://maven.apache.org/download.cgi) to generate java class and jar file.

Tested and developed under Linux Ubuntu 16. We highly recommend to use Linux to prevent unknow bugs.

first clone the project : 
```sh
$ git clone https://github.com/0xMirasio/WebChat.git && cd WebChat/AgentChat
$ chmod +x *.sh
```

If you want to start project (jar already ready in target/ dir).

on Linux
```sh
$ ./start.sh
```
on Windows
```cmd
start.bat
```

to build the project : 

On Linux
```sh
$ ./build.sh
```

On Windows
```cmd
build.bat
```

If you want to clear your cache sessions (too much cache sessions can lead to lag with DB parsing)
Linux
```sh
$ ./clear.sh
```

### KNOW-BUG

- if you close a chat session jframe (from server side), if you reopen the same chat session the jframe will completely bug and nothing will be printed, even is message is receved.
Solution : restart application

- Similar to the previous bug (jframe problem), if a person A contact B, then a person C contact B, the message from C will not pop up on the jframe created for the chat session (B-C) and will go on the chat session for (A-B). The first JFrame is taking all the content, the others jframes will not get any informations. 
Solution : No solution found 

### KNOW SECURITY-ISSUE
- all message aren't encrypted, so a attacker could make a Man In The Middle attack.
- .cache/profile.private isn't secure, the hash can be replaced by another hashed password,so an attacker could login with changing profile.private
- [CRITICAL] : COMMAND INJECTION is possible in FileOperation.java, by making a specific path, it is possible to execute command when upload fonction is formating raw file data into base64 text. 

