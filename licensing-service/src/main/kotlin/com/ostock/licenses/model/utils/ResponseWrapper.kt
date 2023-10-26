package com.ostock.licenses.model.utils

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseWrapper(val data: Any?, val metadata: Any, val errors: List<ErrorMessage>)
