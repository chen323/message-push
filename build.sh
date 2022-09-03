git pull

rm -rf ./wrapper/mylib/*
rm -rf ./wrapper/myconf/*

mvn clean package -Dmaven.test.skip=true

## 线上环境使用
cp -rf ../release-config/*  ./wrapper/myconf/