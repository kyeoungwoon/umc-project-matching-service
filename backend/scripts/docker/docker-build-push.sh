#!/bin/bash

set -e  # 에러 발생 시 스크립트 중단

# =================================
# 설정 변수
# =================================
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"  # backend 디렉토리
DOCKERFILE_PATH="$SCRIPT_DIR/dockerfile"
ENV_FILE="$SCRIPT_DIR/.env"

# =================================
# 색상 코드
# =================================
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# =================================
# 로깅 함수
# =================================
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

log_section() {
    echo ""
    echo "======================================"
    echo -e "${BLUE}$1${NC}"
    echo "======================================"
}

# =================================
# 환경변수 로드
# =================================
load_env() {
    log_section "🔐 환경변수 설정"

    if [ -f "$ENV_FILE" ]; then
        # shellcheck disable=SC2046
        export $(grep -v '^#' "$ENV_FILE" | xargs)
        log_success ".env 파일에서 환경변수 로드 완료"
    else
        log_warning ".env 파일을 찾을 수 없습니다."
    fi

    # 필수 환경변수 확인
    if [ -z "$DOCKER_HUB_USERNAME" ]; then
        log_error "DOCKER_HUB_USERNAME 환경변수가 설정되지 않았습니다."
        exit 1
    fi

    if [ -z "$DOCKER_HUB_ACCESS_TOKEN" ]; then
        log_error "DOCKER_HUB_ACCESS_TOKEN 환경변수가 설정되지 않았습니다."
        exit 1
    fi

    # 기본값 설정
    DOCKER_IMAGE_NAME="${DOCKER_IMAGE_NAME:-spring-boot-app}"
    DOCKER_IMAGE_TAG="${DOCKER_IMAGE_TAG:-latest}"
    BUILD_PLATFORMS="${BUILD_PLATFORMS:-linux/amd64,linux/arm64}"

    log_success "환경변수 확인 완료"
    log_info "이미지명: $DOCKER_HUB_USERNAME/$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG"
    log_info "빌드 플랫폼: $BUILD_PLATFORMS"
    log_info "빌드 컨텍스트: $PROJECT_ROOT"
    log_info "Dockerfile: $DOCKERFILE_PATH"
}

# =================================
# Docker Buildx 설정
# =================================
setup_buildx() {
    log_section "🔧 Docker Buildx 설정"

    # Buildx 빌더가 이미 존재하는지 확인
    if docker buildx inspect multiarch-builder > /dev/null 2>&1; then
        log_info "기존 빌더 사용: multiarch-builder"
        docker buildx use multiarch-builder
    else
        log_info "새로운 빌더 생성: multiarch-builder"
        docker buildx create --name multiarch-builder --use
        docker buildx inspect --bootstrap
    fi

    log_success "Docker Buildx 설정 완료"
}

# =================================
# Docker Hub 로그인
# =================================
docker_login() {
    log_section "🔐 Docker Hub 로그인"

    echo "$DOCKER_HUB_ACCESS_TOKEN" | docker login \
        --username "$DOCKER_HUB_USERNAME" \
        --password-stdin

    log_success "Docker Hub 로그인 성공"
}

# =================================
# 이미지 빌드 및 푸시
# =================================
build_and_push() {
    log_section "🚀 Docker 이미지 빌드 및 푸시"

    # 빌드 시작 시간 기록
    START_TIME=$(date +%s)

    # backend 디렉토리를 빌드 컨텍스트로 사용
    docker buildx build \
        --platform "$BUILD_PLATFORMS" \
        --tag "$DOCKER_HUB_USERNAME/$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG" \
        --tag "$DOCKER_HUB_USERNAME/$DOCKER_IMAGE_NAME:latest" \
        --file "$DOCKERFILE_PATH" \
        --push \
        --progress=plain \
        "$PROJECT_ROOT"

    # 빌드 완료 시간 계산
    END_TIME=$(date +%s)
    ELAPSED_TIME=$((END_TIME - START_TIME))

    log_success "Docker 이미지 빌드 및 푸시 완료 (소요 시간: ${ELAPSED_TIME}초)"
}

# =================================
# 빌드 정보 출력
# =================================
print_build_info() {
    log_section "📦 빌드 정보"

    echo "이미지: $DOCKER_HUB_USERNAME/$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG"
    echo "플랫폼: $BUILD_PLATFORMS"
    echo ""
    echo "Docker Hub에서 확인:"
    echo "https://hub.docker.com/r/$DOCKER_HUB_USERNAME/$DOCKER_IMAGE_NAME"
    echo ""
    echo "이미지 Pull 명령어:"
    echo "docker pull $DOCKER_HUB_USERNAME/$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG"
    echo ""
    echo "컨테이너 실행 명령어:"
    echo "docker run -p 8080:8080 $DOCKER_HUB_USERNAME/$DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG"
}

# =================================
# 메인 실행
# =================================
main() {
    log_section "🎬 Spring Boot Docker 빌드 시작"

    load_env
    setup_buildx
    docker_login
    build_and_push
    print_build_info

    log_section "🎉 전체 프로세스 완료"
}

# 스크립트 실행
main "$@"