#!/bin/bash
git init .
git remote add origin git@github.com:doobo/notes.git
git add .
git commit -m "版本更新"
git push origin master --force

git remote add gitee git@gitee.com:doobo/notes.git
git push gitee master --force

#mvn clean

#mvn clean install -P release
