package com.ostock.licenses.controllers

import com.ostock.licenses.model.utils.ErrorMessage
import com.ostock.licenses.model.utils.ResponseWrapper
import com.ostock.licenses.model.utils.RestErrorList
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.Collections.singletonMap

@ControllerAdvice
@EnableWebMvc
class ExceptionController : ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun handleException(
        request: HttpServletRequest,
        responseWrapper: ResponseWrapper,
    ): ResponseEntity<ResponseWrapper> {
        return ResponseEntity.ok(responseWrapper)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleIOException(request: HttpServletRequest?, e: RuntimeException): ResponseEntity<ResponseWrapper> {
        val errorList = RestErrorList(HttpStatus.NOT_ACCEPTABLE, ErrorMessage(e.message!!, e.message!!))
        val responseWrapper = ResponseWrapper(null, singletonMap("status", HttpStatus.NOT_ACCEPTABLE), errorList)
        return ResponseEntity.ok(responseWrapper)
    }
}
