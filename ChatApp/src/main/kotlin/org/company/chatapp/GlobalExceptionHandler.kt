package org.company.chatapp

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

data class ErrorResponse(
    val message: String,
    val status: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatus(ex: ResponseStatusException): ResponseEntity<ErrorResponse> {
        val status = ex.statusCode.value()
        val error = ErrorResponse(
            message = ex.reason ?: "Lỗi hệ thống",
            status = status
        )
        return ResponseEntity(error, HttpStatus.valueOf(status))
    }


    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = ex.message ?: "Lỗi không xác định",
            status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleOtherExceptions(ex: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = "Đã xảy ra lỗi: ${ex.message}",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
