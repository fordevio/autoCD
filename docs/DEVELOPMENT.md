# Running locally


### Using docker

Prerequisite:
* Operating system: Linux or macos, for windows install [wsl](https://learn.microsoft.com/en-us/windows/wsl/install)  with ubuntu-24.04

* Install [make](https://ioflood.com/blog/install-make-command-linux/)
```
## Commands only for ubuntu
sudo apt install make
```
* Install and run [docker](https://docs.docker.com/engine/install/)
```
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
```
* Add docker user to sudo group
```
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
```

Set up the project:
```
## Clone the git repository
git clone https://github.com/fordevio/autoCD.git

## Go to the wharf directory
cd autoCD

## Make docker image
make dockerImage

## Run docker container
make runDockerWharf
```

The application can be acessed by the url `http://localhost:5001`, the react application can be accessed by the url `http://localhost:3000` on browser

### Make commands
Run `make help` to get all the make commands.

## API Refs:
Api reference can be found in `http://localhost:9001/docs/api` after running the project.