package com.dennis.ecommerce.catalogService.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    // ── 404 ───────────────────────────────────────────────
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(
        ex: NotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Not found: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(
                status = 404,
                error = "Not Found",
                message = ex.message ?: "Recurso no encontrado",
                path = request.requestURI
            ))
    }

    // ── 409 ───────────────────────────────────────────────
    @ExceptionHandler(DuplicateResourceException::class)
    fun handleDuplicate(
        ex: DuplicateResourceException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Duplicate resource: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponse(
                status = 409,
                error = "Conflict",
                message = ex.message ?: "El recurso ya existe",
                path = request.requestURI
            ))
    }

    // ── 422 ───────────────────────────────────────────────
    @ExceptionHandler(BusinessException::class)
    fun handleBusiness(
        ex: BusinessException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Business error: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ErrorResponse(
                status = 422,
                error = "Unprocessable Entity",
                message = ex.message ?: "Error de negocio",
                path = request.requestURI
            ))
    }

    // ── 400 — errores de validación ───────────────────────
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ValidationErrorResponse> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val field = (error as? FieldError)?.field ?: error.objectName
            field to (error.defaultMessage ?: "Valor inválido")
        }
        log.warn("Validation error: $errors")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ValidationErrorResponse(
                status = 400,
                error = "Bad Request",
                message = "Error de validación",
                path = request.requestURI,
                errors = errors
            ))
    }

    // ── 400 — argumento inválido ──────────────────────────
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Illegal argument: ${ex.message}")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(
                status = 400,
                error = "Bad Request",
                message = ex.message ?: "Argumento inválido",
                path = request.requestURI
            ))
    }

    // ── 500 ───────────────────────────────────────────────
    @ExceptionHandler(Exception::class)
    fun handleGeneral(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        log.error("Unexpected error: ${ex.message}", ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(
                status = 500,
                error = "Internal Server Error",
                message = "Error interno del servidor",
                path = request.requestURI
            ))
    }
}