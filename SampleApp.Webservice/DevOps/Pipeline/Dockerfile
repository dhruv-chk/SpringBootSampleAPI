# Jenkins agent image

ARG BASE_IMAGE=maven
ARG IMAGE_TAG=3-openjdk-11



FROM $BASE_IMAGE:$IMAGE_TAG

RUN apt update && \
    apt upgrade -y

RUN apt install -y  \
    curl \
    unzip \
    groff \ 
    less \
    python3 \
    python3-pip

RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip -qq awscliv2.zip && \
    ./aws/install

RUN curl -L "https://github.com/aws/aws-sam-cli/releases/latest/download/aws-sam-cli-linux-x86_64.zip" -o "aws-sam-cli.zip" && \
    unzip -qq aws-sam-cli.zip -d sam-installation && \
    ./sam-installation/install

RUN python3 --version && \
    aws --version && \
    sam --version

RUN curl -fsSL https://get.docker.com -o get-docker.sh
RUN sh get-docker.sh