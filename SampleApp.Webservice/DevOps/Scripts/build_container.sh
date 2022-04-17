#!/bin/bash

docker build -f ./DevOps/Docker/Dockerfile \
    --build-arg AWS_ACCOUNT_ID=$AWS_ACCOUNT_ID \
    --build-arg AWS_REGION=$AWS_DEFAULT_REGION \
    -t $ECR_REPO:$IMAGE_TAG .

docker tag $ECR_REPO:$IMAGE_TAG $AWS_ACCOUNT_ID.dkr.ecr.$AWS_DEFAULT_REGION.amazonaws.com/$ECR_REPO:$IMAGE_TAG
