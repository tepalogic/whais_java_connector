#!/bin/bash

TESTS=`find ./ -name "ConnectorTest*.class" | grep -v '\\$'`


if [ -z "${TESTS}" ]; then
	echo "No tests found.";
	exit 0;
fi

echo "The tests are: "  ${TESTS}

for frame_size in `seq 2048 1 2303` `seq 2304 64 65535` 65535
do
	echo Testting for frame size ${frame_size}:
	for test in ${TESTS}
	do
		t=$(basename $test .class)
		echo -ne "\t${t} ..."
		java ${t} $@ --fs ${frame_size} &> /dev/null
		if [ $? -ne 0 ]; then
			echo "FAIL"
			echo -e "\t\tFailed to test 'java ${t} $@ --fs ${frame_size}'"
			exit 1
		fi
		echo "PASS"
	done

done

	

