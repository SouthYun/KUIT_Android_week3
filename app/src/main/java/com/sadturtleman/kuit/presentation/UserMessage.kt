package com.sadturtleman.kuit.presentation

import retrofit2.HttpException
import java.io.IOException

// 미션 B — 예외 → 사용자 문구. 세 ViewModel 이 이 함수 하나를 같이 씀
fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> "인터넷 연결을 확인하세요"        // 비행기 모드, 서버 주소 못 찾음
    is HttpException -> "서버 오류 (${code()})"         // 서버가 4xx / 5xx 로 응답
    else -> "알 수 없는 오류"
}