#!/bin/bash

# check for missing files: start , finish, REAMDE.adoc.

# echo -n -e "\033[1;34mPlease enter repository name: \033[0m"
# read -e repoName
# if [ ! -d "./$repoName" ]
# then
#    echo -e "\033[1;31mError: directory ./$repoName does not exists.\033[0m"
#    exit
# fi

# cd $repoName

echo -e "\033[1;32m\nChecking the differences in start and finish directories...\n\033[0m"
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

##############################################################

echo -e "\033[1;32mChecking the files to be commited:\n\033[0m"

committedFiles=$(git diff --stat --cached origin/master)

echo -e "$committedFiles\n"

##############################################################

# For structure: https://unix.stackexchange.com/questions/266757/go-through-every-subfolder-check-for-a-folder-and-run-script/266800
# https://github.com/goodmami/check.bash
# find A -type d -name target -execdir foo.sh \;

# https://stackoverflow.com/questions/29967444/read-multiple-inputs-and-store-in-a-array-then-print-the-values-from-that-array

# done=false
# startFiles=()
# echo
# for ((i=1;; i++)); do
# 	read -p $'\033[1;34mPlease enter the file(s) that should not be in start directory: \033[0m' startFiles[$i]
# 	break
# done


# for file in "${startFiles[@]}"
# do
# 	find "./start" -name "$file" | echo -e "\033[1;31mError: $file not found\033[0m"
# done

# declare -a words=('we ' 'you will' 'lets' 'let us' "let\'s" 'has been' 'have been' 'was')
# clean=true
# for word in "${words[@]}"
# do
# 	wordCount=$(grep -o -i "$word" README.adoc | wc -l | sed -e 's/^[ \t]*//')
# 	linesNumber=$(grep -i -n "$word" README.adoc | cut -f1 -d:)
# 	if [ "$wordCount" -ge 1 ]
# 	then
# 	echo -e "You have $wordCount words of \033[1;31m'$word'\033[0m in README.adoc file at following lines:"
# 	echo -e "$linesNumber\n"
# 	clean=false
# 	fi
# done

# if [ clean == true ]
# then
# echo "README.adoc is clean"
# fi

# #################################################################

# ### change `/src -> `src
# sed -i -e 's/`\/src/`src/g' README.adoc
# echo "Changed /src -> src"

# actualPaths=($(find "finish" -type f | cut -c8-))
# writtenPaths=($(grep -o '`src.*`' README.adoc | sed -e 's/^`//' -e 's/`$//'))
# for writtenPath in "${writtenPaths[@]}"
# do
# 	for actualPath in "${actualPaths[@]}"
# 	do
# 		writtenFile=$(echo "$writtenPath" | rev | cut -d'/' -f1 | rev)
# 		actualFile=$(echo "$actualPath" | rev | cut -d'/' -f1 | rev)
# 		if  [ "$writtenFile" == "$actualFile" ]
# 		then
# 			if [ "$writtenPath" != "$actualPath" ]
# 			then
# 			echo -e "\033[1;31mWARNING:\033[0m $writtenPath \033[1;31m<-->\033[0m $actualPath"
# 			fi
# 		fi
# 	done
# done

# grep -i 'create.*in.*the.*`src.*`.*file.*:' README.adoc
