![Logo](assets/autocd-logo.png)

# autoCD

![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![NodeJS](https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![TypeScript](https://img.shields.io/badge/typescript-%23007ACC.svg?style=for-the-badge&logo=typescript&logoColor=white)

# Overview

autoCD is a Continuous Delivery automation platform designed for monolithic servers. It provides a web-based interface for managing deployment scripts, users, and projects, with automated execution of CD pipelines through a multi-threaded script runner. The system combines a React frontend with a Spring Boot backend to deliver a complete solution for automating software deployments.

### Features

autoCD automates the continuous delivery process by:

* Managing deployment scripts stored as shell files in /var/autocd/scripts/
* Providing user authentication and permission management via JWT tokens
* Executing deployment scripts through a dynamic thread pool system
* Tracking execution status and logging output to /var/autocd/logs/
* Offering a web interface for project management and script editing


# Project Status

Current the version 1.3.1 is released with the features above, upcoming relases will work on solving frontend bugs and inproving UI.

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
VERSION=1.3.1
wget https://github.com/fordevio/autoCD/releases/download/v$VERSION/autocd-$VERSION.zip
unzip autocd-$VERSION.zip
sudo nohup java -jar autocd-$VERSION.jar > output.log 2>&1 &
```
Admin credintials:
- Username: "admin"
- Password: "admin"

Change admin credentials after first login.

Admin credential is stored in the file "/var/autocd/admin-credential.json", in case you forgot. 

The application is running in http://localhost:5001, Logs of autocd can be seen in output.log

- Stop running autocd

```
PID=$(sudo lsof -t -i :5001)
sudo kill -9 $PID
```

[Good blog](https://medium.com/@fordev951/automating-cd-with-autocd-44adc1bad222)  to get hands on experince with autoCD.

## ⭐️ Support

If you find this project helpful or interesting, please give it a ⭐️ on [GitHub](https://github.com/fordevio/autoCD)! Your support helps the project grow and motivates community to keep improving it.
Also join the [#autoCD](https://fordev-io.slack.com/archives/C086UJZ4658) slack channel.

# Contributing

Visit [CONTRIBUTING.md](./docs/CONTRIBUTING.md) for contributions guidlines. 

# AI Docs

https://deepwiki.com/fordevio/autoCD

# Acknowledgements

autoCD logo is generated using ChatGPT.