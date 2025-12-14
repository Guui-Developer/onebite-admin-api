#!/bin/bash

# [추가됨] 스크립트 실행 시 무조건 서울 리전을 바라보도록 강제 설정
export AWS_REGION=ap-northeast-2
export AWS_DEFAULT_REGION=ap-northeast-2

# 색상 정의
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. 환경 변수 확인 (dev 또는 prod)
ENV=$1

if [ -z "$ENV" ]; then
    echo -e "${RED}오류: 배포할 환경을 입력해주세요.${NC}"
    echo -e "사용법: ./deploy.sh [dev|prod]"
    exit 1
fi

if [[ "$ENV" != "dev" && "$ENV" != "prod" ]]; then
    echo -e "${RED}오류: 올바르지 않은 환경입니다. (dev 또는 prod만 가능)${NC}"
    exit 1
fi

echo -e "${GREEN}=== Spring Boot Lambda 배포 시작 (환경: ${YELLOW}${ENV}${GREEN}) ===${NC}"

# 2. Gradle 빌드
echo -e "${GREEN}[1/3] Gradle 빌드 중...${NC}"
./gradlew clean build -x test
if [ $? -ne 0 ]; then
    echo -e "${RED}Gradle 빌드 실패${NC}"
    exit 1
fi

# 3. SAM 빌드
echo -e "${GREEN}[2/3] SAM 빌드 중...${NC}"
sam build
if [ $? -ne 0 ]; then
    echo -e "${RED}SAM 빌드 실패${NC}"
    exit 1
fi

# 4. SAM 배포 (환경별 config 사용)
echo -e "${GREEN}[3/3] SAM 배포 중... (Config: ${ENV})${NC}"
sam deploy --config-env ${ENV}
if [ $? -ne 0 ]; then
    echo -e "${RED}SAM 배포 실패${NC}"
    exit 1
fi

echo -e "${GREEN}=== ${ENV} 환경 배포 완료! ===${NC}"