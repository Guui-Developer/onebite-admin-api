#!/bin/bash

# 색상 정의
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. 환경 인자 확인 (dev 또는 prod)
ENV=$1
OPTION=$2

if [ -z "$ENV" ]; then
    echo -e "${RED}오류: 환경을 입력해주세요.${NC}"
    echo -e "사용법: ./logs.sh [dev|prod] (옵션: error)"
    exit 1
fi

if [[ "$ENV" != "dev" && "$ENV" != "prod" ]]; then
    echo -e "${RED}오류: 올바르지 않은 환경입니다. (dev 또는 prod만 가능)${NC}"
    exit 1
fi

# 2. 스택 이름 동적 생성
STACK_NAME="onebite-admin-app-${ENV}"

echo -e "${BLUE}=== [${YELLOW}${ENV}${BLUE}] 환경 로그 확인 중 (Stack: ${STACK_NAME}) ===${NC}"

# 3. 옵션에 따른 로그 출력
case "$OPTION" in
    "error")
        echo -e "${RED}❌ 에러 로그 필터링 중...${NC}"
        sam logs --stack-name $STACK_NAME --tail --filter "ERROR"
        ;;
    *)
        echo -e "${BLUE}📊 실시간 로그 스트리밍 중...${NC}"
        sam logs --stack-name $STACK_NAME --tail
        ;;
esac