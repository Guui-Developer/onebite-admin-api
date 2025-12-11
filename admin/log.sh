#!/bin/bash

# 색상 정의
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

STACK_NAME="onebite-admin-app"

case "$1" in
    "error")
        echo -e "${RED}❌ 에러 로그 필터링...${NC}"
        sam logs --stack-name $STACK_NAME --tail --filter "ERROR"
        ;;
    *)
        echo -e "${BLUE}📊 실시간 로그 스트리밍...${NC}"
        sam logs --stack-name $STACK_NAME --tail
        ;;
esac