package com.ostock.licenses.model.utils

import org.springframework.http.HttpStatus

class RestErrorList(var status: HttpStatus, vararg errors: ErrorMessage) : ArrayList<ErrorMessage>() {
    init {
        this.addAll(errors)
    }
}
