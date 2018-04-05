#!/bin/bash

echo -e "\033[1;32mChecking the differences in start and finish directories...\033[0m"
diff -r start finish

##################################################################

# echo -e "\033[1;32m\nRemoving cached files in .git directory\n\033[0m"

# declare -a files=("zip" "jar" "DS_Store" "target")

# for i in "${files[@]}"
# do
#   if [ "$i" == "target" ]
#   then
#     git filter-branch --index-filter "git rm --cached --ignore-unmatch -r $i" -- --all
#   else
#     git filter-branch --index-filter "git rm --cached --ignore-unmatch *.$i" -- --all
#   fi
#   rm -Rf .git/refs/original
#   rm -Rf .git/logs/
#   git gc --aggressive --prune=now
# done

# output=$(du -hks)
# size=$(echo $output | cut -c1-3)

# if [ $size -gt 1000 ]
# then
#    echo -e "\033[1;31m\nYour repository is large.\n\033[0m"
#    exit
# fi

# echo -n -e "\033[1;34mDo you want to push(Y/N)?  \033[0m"
# read -e answer
# if [ "$answer" == "Y" ] || [ "$answer" == "y" ] ; then
#  echo -e "Pushed...\n"
# else
#  echo -e ""
# fi

#############################################################

declare -a words=('we ' 'you will' 'lets' 'let us' "let\'s" 'has been' 'have been' 'was')
clean=true
for word in "${words[@]}"
do
	wordCount=$(grep -o -i "$word" README.adoc | wc -l | sed -e 's/^[ \t]*//')
	linesNumber=$(grep -i -n "$word" README.adoc | cut -f1 -d:)
	if [ "$wordCount" -ge 1 ]
	then
	echo -e "You have $wordCount words of \033[1;31m'$word'\033[0m in README.adoc file at following lines:"
	echo -e "$linesNumber\n"
	clean=false
	fi
done

if [ clean == true ]
then
echo "README.adoc is clean"
fi

#################################################################

sed -i -e 's/`/,/g' README.adoc
git add *
git commit -m "Travis-Ci Pushed..."
git push 

actualPaths=($(find "finish" -type f | cut -c8-))
writtenPaths=($(grep -o '`src.*`' README.adoc | sed -e 's/^`//' -e 's/`$//'))
for writtenPath in "${writtenPaths[@]}"
do
	for actualPath in "${actualPaths[@]}"
	do
		writtenFile=$(echo "$writtenPath" | rev | cut -d'/' -f1 | rev)
		actualFile=$(echo "$actualPath" | rev | cut -d'/' -f1 | rev)
		if  [ "$writtenFile" == "$actualFile" ]
		then
			if [ "$writtenPath" != "$actualPath" ]
			then
			echo -e "\033[1;31mWARNING:\033[0m $writtenPath \033[1;31m<-->\033[0m $actualPath"
			fi
		fi
	done
done

grep -i 'create.*in.*the.*`src.*`.*file.*:' README.adoc
