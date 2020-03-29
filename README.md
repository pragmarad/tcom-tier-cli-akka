TCP Akka Streams Client POC
==================================
Inception year: **2020**

## Overview
It's client tier for akka streams TCP PoC. Idea is to run stream capable client using akka Tcp extension.
Having different variations of response from TCP server different cases of handling streams can be checked.
Overall, the goal is to validate how well TCP akka streams work in various loads. 

## Build
To build project, sbt 1.3.8+ is needed. Install sbt launcher and add setup dir in $PATH.
If you want to check cross compilation, use commands with '+' e.g. for compile:
```
sbt '+ compile'
```

## Run
Using sbt with all defaults:
```
sbt "runMain tech.pragmarad.tcom.client.TcpAkkaStreamClientApp"
```
Also, with sample arguments:
```
sbt "runMain tech.pragmarad.tcom.client.TcpAkkaStreamClientApp --srvhost localhost --srvport 1661 --message tst1 --frequencymsecs 1000 --maxburstcount 10"
```
Flexibility of input parameters allows creating different test cases for TCP akka streaming.


## Test
For unit testing usual junit can be used:
```
sbt test
```

## Publish
In order to publish in local repo you can use:
```
sbt publishLocal
```
To publish into  [bintray repo](https://bintray.com/pragmarad-tech/tcom-scala-akka/tcom-tier-cli-akka), use:
```
sbt publish
```

## Release
In order to release new version use:
```
sbt release
```

## Usage
### Config loading
Since commons version 0.1.1 you can use **-Dtcom.conf.file=other_tcom.conf** to provide with own config file name. 
By default, **application.tcom.conf** will be used (along with system properties).

## Logging
SLF4J with logback impl used. If you need to provide with custom log file (can be handy for different env-s), you can use 
system property in form of **-Dlogback.configurationFile=/path/to/custom_logback_config.xml**.

# Links

## Status
* 2020-03-07 - 0.0.1-SNAPSHOT - Init.
* 2020-03-25 - 0.1.0 - Args handling added.
* 2020-03-29 - 0.1.1 - commons version 0.1.1 set (with tcom.conf.file property support).

# Roadmap
1. (DONE) Add basic TCP client with untyped actors use.
2. (DONE) Make port (and host?) configurable (Args->env var -> app conf -> constants).
3. Replace untyped to typed actors.
4. Update more complicated message handling (imagine protocols with timeouts, errors, traffic overloads).
5. Prepare for local deployments.
6. Prepare for containers (docker/k8s).
