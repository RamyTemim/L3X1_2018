#!/bin/bash

cp -R ~/L3X1_2018/AWS_API ~/2017-l3x1/branches/

cd ~/2017-l3x1/

svn upgrade

echo "Entre le message à committer séparé par des _ : "
read message

svn commit -m $message


