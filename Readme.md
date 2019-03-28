# JWT Project

## Configuration List
- ***Version***
  - Java 8
  - org.springframework.version 4.0.0.RELEASE
  - JDK 1.8
- ***root-context.xml***
  - context-properties.xml
  - context-message
  - context-db

## Function List
- AES, HASH, RSA Encryption Module
- Http Communication Module

## JWT
- ***JWT?***
  - JSON Web Token
  - Token 기반의 인증이 많아지면서 HTTP Authorization 헤더나 URI, 쿼리 파라미터 등 공백을 사용할 수 없는 곳에서 가볍게 사용할 수 있는 토큰으로 설계.
- ***JWT Configuration***
  - header.payload.signature
  - header : 토큰에 대한 설명
  - payload : JWT의 권한 claims를 담고 있음
  - signature : 토큰의 무결성을 검증하기 위한 시그너처 해쉬
- ***JWT Claim Set***
  - iss : 토큰을 발급한 발급자 (Issuer)
  - sub : Claim의 주제(Subject)로 토큰이 갖는 문맥을 의미
  - aud : 이 Token을 사용할 수신자 (Audience)
  - exp : 만료시간(Expiration Time)은 만료시간이 지난 토큰은 거절해야 함
  - nbf : Not Before의 의미로 이 시간 이전에는 토큰을 처리하지 않아야 함
  - iat : Token이 발급된 시간 (Issued At)
  - jti : JWT ID로 토큰에 대한 식별자
