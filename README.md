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

## Assembling
In order to prepare new assembly (as fat Jar) use:
```shell
sbt assembly
```
It will generate jar with name <tt>${name}-assembly-${version}.jar</tt> in **target/scala-2.13/**
(where 'name' is project name, 'version' - project version e.g. tcom-tier-cli-akka-assembly-0.1.1.jar).

This jar can be can be used in container deployments (e.g. Deocker image can be built). One of small challenges can be a fact all configs are burried in this jar.

## Deployments
There are multiple ways to deploy this app. It was specifically built for flexible deployments.
Some variants are shown under [deployments](deployments) folder.

## Usage

### Launch assembly
The fastest way to launch the app is to put a jar into environment where JRE 11+ is present (JRE8 may work too, but was not checked) and call:
```shell
java -jar ${name}-assembly-${version}.jar
```

For example, with jar name **tcom-tier-cli-akka-assembly-0.1.1.jar** a call may look like this (for custom host and post as arguments): 
```shell
java -jar tcom-tier-cli-akka-assembly-0.1.1.jar --srvhost localhost --srvport 1661 --message tst1 --frequencymsecs 1000 --maxburstcount 10
```

As application config(see below) supports env variables, this way of passing options can be used as well:
```shell
TCOM_SRV_HOST='localhost'
TCOM_SRV_PORT='1661'
java -jar tcom-tier-cli-akka-assembly-0.1.1.jar
```

### Config loading
Since commons version 0.1.1 you can use **-Dtcom.conf.file=other_tcom.conf** to provide with own config file name. 
By default, **application.tcom.conf** will be used (along with system properties).

NOTE: fat jar deployment has own classpath, so config files external to it won't be seen if to use 'java -jar'.

### Logging
SLF4J with logback impl used. If you need to provide with custom log file (can be handy for different env-s), you can use 
system property in form of **-Dlogback.configurationFile=/path/to/custom_logback_config.xml**.

NOTE: fat jar deployment has own classpath, so config files external to it won't be seen if to use 'java -jar'.

### Using custom configs with fat jar
Because 'java -jar some.jar' implies that 'some.jar' supposed to have all classpath info at 'MANIFEST/META-INF'
, using any external classpath entries (-cp ${EXT_CLASS_PATH}) is hard (likely mentioned META-INF needs additions/hacks).
To allow external configs, usual main class based lauch can be used.
Imagine basic dir structure, with: 
- **conf** - contains configs, such as **application.tcom.cli.conf**, **logback.cli.xml**.
- **lib** - contains jars, for now just one assembly **tcom-tier-cli-akka-assembly-0.1.1.jar** (copied from target dir after 'sbt assembly').

Then we can write app start script (let's name it 'start_cli.sh'), on primitive level:
```shell
#!/usr/bin/env bash
#
MAIN_CLASS='tech.pragmarad.tcom.client.TcpAkkaStreamClientApp'
CPATH='conf:lib/tcom-tier-cli-akka-assembly-0.1.1.jar'
APP_JVM_OPTS="-Xms128M -Xmx256M"
APP_CONF_FILE='application.tcom.cli.conf'
LOG_CONF_FILE='logback.cli.xml'
CONF_JVM_OPTS="-Dtcom.conf.file=${APP_CONF_FILE} -Dlogback.configurationFile=${LOG_CONF_FILE}"
	
java ${APP_JVM_OPTS} ${CONF_JVM_OPTS} -cp ${CPATH} ${MAIN_CLASS} "$@"
```

This script can be called with arguments such as:
```shell
. start_cli.sh --srvhost localhost --srvport 1661 --message tst1 --frequencymsecs 1000 --maxburstcount 10
```

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
