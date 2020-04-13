#!/usr/bin/env bash
#
# This script launches a TCOM cli app.
#

# Initializes variables.
function init_env() {
	APP_NAME='tcom-tier-cli-akka'
	# RUNTIME_ROOT_DIR - Parent of current script dir is assumed a root runtime one.
	RUNTIME_ROOT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../" && pwd )"

	JAVA_EXECUTABLE="`which java`"

	LIB_DIR="${RUNTIME_ROOT_DIR}/lib"
	CONF_DIR="${RUNTIME_ROOT_DIR}/conf"
	MAIN_CLASS='tech.pragmarad.tcom.client.TcpAkkaStreamClientApp'

	# Prepare config
	APP_CONF_FILE='application.tcom.cli.conf'
	LOG_CONF_FILE='logback.cli.xml'
	
	# Prepare config JVM options
	CONF_JVM_OPTS="-Dtcom.conf.file=${APP_CONF_FILE} -Dlogback.configurationFile=${LOG_CONF_FILE}"
}

# Validates Java executable.
function validate_java() {
	if [ -z "${JAVA_EXECUTABLE}" ]; then
    	echo "JAVA_EXECUTABLE is unset or set to the empty string, please set it and restart the app." >&2
    	return 1
	fi
}

# Builds classpath in form of CPATH variable
function build_cpath() {
	# Use it if multiple files are expected in lib dir.
	local classpath="${CONF_DIR}"
	# o Cycle to grab all jars in lib dir
	local cpath_files="$LIB_DIR/*"
	for cpathFile in $cpath_files
	do
	  classpath="$classpath:$cpathFile"
	done
	readonly CPATH="$classpath"
}

# Starts app. 
# NOTE: TCOM_JVM_OPTS can be set outside this script (should be exported?). Example:
#       export TCOM_JVM_OPTS="-Xms128M -Xmx256M"
function launch() {
	echo "oo >>>> Starting ${APP_NAME} with options:"
	echo "oo   TCOM_JVM_OPTS: ${TCOM_JVM_OPTS}"
	echo "oo   CONF_JVM_OPTS: ${CONF_JVM_OPTS}"
	echo "oo   CPATH: ${CPATH}"
	echo "oo   MAIN_CLASS: ${MAIN_CLASS}"
	echo "oo   Arguments: $@"
	$JAVA_EXECUTABLE ${TCOM_JVM_OPTS} ${CONF_JVM_OPTS} -cp ${CPATH} ${MAIN_CLASS} "$@"
	echo "oo >>>> Working ${APP_NAME} done. Status: $?"
}


# Prepares env and launces the app
function main() {
	local script_name=$0
	local func_name="${script_name}/main"
	echo "oo $func_name Starting app with args $@"
	echo "oo $func_name Stage 1) init_env"
	init_env	

	echo "oo $func_name Stage 2) validate_java"
	validate_java

	echo "oo $func_name Stage 3) build_cpath"
	build_cpath	

	echo "oo $func_name Stage 4) launch"
	launch "$@"
}

main "$@"