#!/bin/sh


  git config --global user.email "ahmadosamaayyoub@gmail.com"
  git config --global user.name "Ahmad-Ayyoub"

  git add . README.adoc
  git commit --message "Travis build: OK"

  git remote add origin-pages https://${GH_TOKEN}@github.com/Ahmad-Ayyoub/draft-guide-microprofile-rest-client.git > /dev/null 2>&1
  git push --quiet --set-upstream origin-pages gh-pages 
