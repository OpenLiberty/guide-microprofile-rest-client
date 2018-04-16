#!/bin/bash
sudo apt-get install maven
cd finish
mvn clean
mavenOutput=$(mvn liberty:install-server)
build=$(echo "$mavenOutput" | grep -i "runtime/" | cut -d'-' -f 2 | cut -d'/' -f 2 | grep -i "1") 
echo -e "\033[1;34m\nOpenLiberty runtime:\033[0m 1$build"
cd ..
#############################################################################################
echo -e "\033[1;32m[1] Checking the differences in start and finish folders ...\033[0m"
status_1=0
if ! diff -r start finish >/dev/null 2>&1; then
  echo -e "\033[1;31mWARNING:\033[0m"
  status_1=1
fi
diff -r start finish
#############################################################################################
echo -e "\033[1;32m\n[2] Checking uncreated folders in start folder ...\033[0m"
status_2=$status_1
result=($(diff -r start finish | sed -e 's/: /\//' | cut -c9-))
for path in "${result[@]}"; do
    if [ -d $path ] && [[ $path = *"finish"* ]]
		then
			directory=$( echo "$path" | rev | cut -d'/' -f1 | rev )
			echo -e "\033[1;31mWARNING:\033[0m The directory \033[1;34m$directory\033[0m is not created in start folder"
		elif [ -d $path ] && [[ $path = *"start"* ]]
		then
			echo -e "\033[1;31mWARNING:\033[0m The directory \033[1;34m$directory\033[0m is found in start folder: $path"
		fi
done
#############################################################################################
echo -e "\033[1;32m\n[3] Checking undesired phrases in README.adoc ...\033[0m"
status_3=0
declare -a words=('we ' 'you will' 'lets' 'let us' "let\'s" 'has been' 'have been' 'was')
for word in "${words[@]}"
do
	wordCount=$(grep -o -i "$word" README.adoc | wc -l | sed -e 's/^[ \t]*//')
	linesNumber=$(grep -i -n "$word" README.adoc | cut -f1 -d:)
	if [ "$wordCount" -ge 1 ]
	then
	echo -e "\033[1;31mWARNING:\033[0m You have $wordCount words of \033[1;31m'$word'\033[0m in README.adoc file at following lines:"
	echo -e "$linesNumber\n"
	status_3=1
	fi
done
#############################################################################################
echo -e "\033[1;32m\n[4] Checking the path of the created files in README.adoc ...\033[0m"
status_4=0
grep -i '`/src.*`' README.adoc | while read -r line ; do
  if [[ ! -z $line ]]; then
    echo -e "\033[1;31mWARNING:\033[0m detected \`/src\` in $line"
    status_4=1
  fi
done
sed -i -e 's/`\/src/`src/' README.adoc
actualPaths=($(find "finish" -type f -path ./target | cut -c8-))
writtenPaths=($(grep -o '`src.*`' README.adoc | sed -e 's/^`//' -e 's/`$//'))
for writtenPath in "${writtenPaths[@]}"
do
	for actualPath in "${actualPaths[@]}"
	do
		writtenFile=$(echo "$writtenPath" | rev | cut -d'/' -f1 | rev)
		actualFile=$(echo "$actualPath" | rev | cut -d'/' -f1 | rev)
		while read -n 1 c; do charArray+=($c); done  <<< "$writtenFile"
    count=0
    writtenLength=${#writtenFile}
    actualLength=${#actualFile}
    lengthDifference=$(($writtenLength - $actualLength))
    if [ $lengthDifference -lt 0 ]; then
      let "lengthDifference = (( 0 - $lengthDifference ))"
    fi
    for char in "${charArray[@]}"
    do
      if [[ ! ($actualFile =~ $char) ]]; then
        let "count += 1"
      fi
    done
    if [[ $count -eq 0 && "$writtenFile" == "$actualFile" ]]; then
      if [ "$writtenPath" != "$actualPath" ]; then
        echo -e "\033[1;31mWARNING:\033[0m $writtenPath \033[1;31m<-->\033[0m $actualPath"
        status_4=1
      fi
    elif [[ $lengthDifference -eq 1 || $lengthDifference -eq 2 ]]; then
    		length=${#charArray[@]}
        subLength=$(($length/2))
        subArray_1=("${charArray[@]:0:$subLength}")
        subArray_2=("${charArray[@]:$subLength:$length}")
        substring_1=$(echo "${subArray_1[*]}" | tr -d ' ' )
        substring_2=$(echo "${subArray_2[*]}" | tr -d ' ')
      if [[ $actualFile =~ $substring_1 || $actualFile =~ $substring_2 ]]; then
        echo -e "\033[1;31mWARNING:\033[0m file name $writtenFile is not correct "
        status_4=1
      fi
    fi
    charArray=()
	done
done
#############################################################################################
echo -e "\033[1;32m\n[5] Checking the format of creating the files in README.adoc ...\033[0m"
status_5=0
while read -r line ; do
line=$(echo "$line" | tr '[:upper:]' '[:lower:]')
  if ! (echo "$line" | grep -iq "create .* in the \`.*\` file:"); then
    echo -e "\033[1;31mWARNING:\033[0m \033[1;34m$line\033[0m is not following the format\033[0m."
    status_5=1
  fi
done < <(grep -i '`src.*`' README.adoc)
if [ $status_5 -eq 1 ]; then
  echo -e "\033[1;34mFormat:\033[0m Create a <class/type> in the \`<path>\` file: ."
fi
#############################################################################################
echo -e "\033[1;32m\n[6] Checking the location of pom.xml ...\033[0m"
status_6=0
OUTPUT=($(find . -name "pom.xml"))
for file in "${OUTPUT[@]}"
do
		if [[ $file =~ "finish" ]] && [ ! "$file" == "./finish/pom.xml" ]; then
      echo -e "\033[1;31mWARNING:\033[0m wrong path for pom.xml: $file "
      echo -e "Please make sure \033[1;31mpom.xml\033[0m file is under the root directory: 'finish' ."
      let status_6=1
    elif [[ $file =~ "start" ]] && [ ! "$file" == "./start/pom.xml" ]; then
      echo -e "\033[1;31mWARNING:\033[0m wrong path for pom.xml: $file "
      echo -e "Please make sure \033[1;31mpom.xml\033[0m file is under the root directory: 'start' ."
      let status_6=1
		fi
done
#############################################################################################
echo -e "\033[1;32m\n[7] Running Acrolinx ...\033[0m"
status_7=0
############################################################################################
echo -e "\033[1;32m\n_______________[ SUMMARY ]_______________\033[0m"
statuses=($status_1 $status_2 $status_3 $status_4 $status_5 $status_6 $status_7)
for ((i = 0; i < ${#statuses[@]}; ++i)); do
  number=$(($i+1))
  if [ ${statuses[$i]} -eq 0 ] ; then
    echo -e "\033[1;32m|              [$number] Passed               |\033[0m"
  else
    echo -e "\033[1;31m|______________[$number] Warning______________|\033[0m"
  fi
done
