#!/usr/bin/env bash

publish_dir=/usr/share/jy

echo 'update project ...'
git pull

echo 'build project ...'
./mvnw -U clean compile install -P product -s .mvn/settings.xml

echo "copy to dir: $publish_dir"
cp target/fibre*.jar $publish_dir
chmod +x $publish_dir/boa*.jar
service fibre restart

echo 'done!'
