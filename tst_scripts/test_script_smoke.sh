#!/bin/bash

TESTS=`find ./ -name "Test*.class" | grep -v '\\$'`
total_tests=0
failed_tests=0


if [ -z "${TESTS}" ]; then
	echo "No tests found.";
	exit 0;
fi

for test in ${TESTS}
do
	let 'total_tests+=1'
	t=$(basename $test .class)
	echo -ne "java ${t} $@ ..."
	java ${t} $@  &> /dev/null
	if [ $? -ne 0 ]; then
		echo -e "'\e[0;31mfailed\e[0m'!"
		let 'failed_tests+=1'
	else
		echo -e "'\e[0;32mpassed\e[0m'!"
	fi
done
	
if [ "$total_tests" -eq 0 ]; then
   echo "I could not find any tests to execute!"
else
   echo "Executed tests: $total_tests"
   echo "Failed tests:   $failed_tests"
fi

