FROM node:12-alpine

WORKDIR /usr/src/executor

RUN apk add --update docker openrc
RUN rc-update add docker boot

COPY package*.json ./

RUN npm install
COPY . .
CMD node .
