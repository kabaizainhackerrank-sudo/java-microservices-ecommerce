package com.dennis.ecommerce.catalogService.exception

import java.time.LocalDateTime

data class ValidationErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val errors: Map<String, String>,
    val timestamp: LocalDateTime = LocalDateTime.now()
)