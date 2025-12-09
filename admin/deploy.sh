#!/bin/bash

# 색상 정의 (선택사항)
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Spring Boot Lambda 배포 시작 ===${NC}"

# 1. Gradle 빌드
echo -e "${GREEN}[1/3] Gradle 빌드 중...${NC}"
./gradlew clean build -x test
if [ $? -ne 0 ]; then
    echo -e "${RED}Gradle 빌드 실패${NC}"
    exit 1
fi

# 2. SAM 빌드
echo -e "${GREEN}[2/3] SAM 빌드 중...${NC}"
sam build
if [ $? -ne 0 ]; then
    echo -e "${RED}SAM 빌드 실패${NC}"
    exit 1
fi

# 3. SAM 배포
echo -e "${GREEN}[3/3] SAM 배포 중...${NC}"
sam deploy
if [ $? -ne 0 ]; then
    echo -e "${RED}SAM 배포 실패${NC}"
    exit 1
fi

echo -e "${GREEN}=== 배포 완료! ===${NC}"