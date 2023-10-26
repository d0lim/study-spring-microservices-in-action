package com.ostock.licenses.model.utils

data class ErrorMessage(
    val message: String,
    val code: String = "",
    val detail: String = "",
)
