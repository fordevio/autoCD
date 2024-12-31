![Logo](assets/autocd-logo.png)

# autoCD
It is a platform to provide automation to CD in monolythic server.

![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![NodeJS](https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white)

# Project Status
Current the version 1.0.0 is released with the features below, upcoming relases will work on solving frontend bugs and inproving UI.

# Features
 - Maintain users
 - Maintain users permissions
 - Maintain projects
 - Project CD script
 - Message queue
 - Autoscalling of threads 
 - Application logs

 # Quickstart

 - Install java-17
   (On ubuntu)

   ```
   apt-get update
   apt-get upgrade
   apt install openjdk-17-jdk openjdk-17-jre
   ```

- Install and run autoCD

Use the value of version you want to install


```
VERSION=1.0.0
wget https://github.com/fordevio/autoCD/releases/download/v$VERSION/autocd-$VERSION.zip
unzip autocd-$VERSION.zip
sudo nohup java -jar autocd-$VERSION.jar > output.log 2>&1 &
```
Admin credintials:
- Username: "admin"
- Password: "admin"

Change admin credentials after first login.

The application is running in http://localhost:5001, Logs of autocd can be seen in output.log

- Stop running autocd

```
PID=$(sudo lsof -t -i :5001)
sudo kill -9 $PID
```


# Contributing to autoCD
 Visit [CONTRIBUTING.md](./docs/CONTRIBUTING.md) for contributions guidlines. 

# Acknowledgements
autoCD logo is generated using ChatGPT.