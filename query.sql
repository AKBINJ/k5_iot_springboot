### k5_iot_springboot >>> query ###

# 1. 스키마 생성 (이미 존재하면 삭제)
DROP DATABASE IF EXISTS k5_iot_springboot;

# 2. 스키마 생성 + 문자셋/정렬 설정
CREATE DATABASE IF NOT EXISTS k5_iot_springboot
	CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;
    
# 3. 스키마 선택
USE k5_iot_springboot;