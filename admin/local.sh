#!/bin/bash

# 색상 정의
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Spring Boot Local API 실행 시작 ===${NC}"

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

# 3. SAM 로컬 실행 (수정된 부분)
echo -e "${GREEN}[3/3] 로컬 API 서버 시작 중... (http://127.0.0.1:3000)${NC}"
echo -e "${GREEN}종료하려면 Ctrl+C를 누르세요.${NC}"

sam local start-api --port 3000