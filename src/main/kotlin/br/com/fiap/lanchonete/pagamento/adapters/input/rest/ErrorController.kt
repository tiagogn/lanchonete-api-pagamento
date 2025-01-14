package br.com.fiap.lanchonete.pagamento.adapters.input.rest

import br.com.fiap.lanchonete.pagamento.adapters.input.rest.response.ErrorResponse
import br.com.fiap.lanchonete.pagamento.core.exceptions.PagamentoException
import br.com.fiap.lanchonete.pagamento.core.exceptions.ResourceNotFoundException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorController {

    val logger = org.slf4j.LoggerFactory.getLogger(ErrorController::class.java)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(e: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
        logger.error("Recurso não encontrado", e)
        return ResponseEntity(
            ErrorResponse(e.message),
            org.springframework.http.HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleResourceNotFoundException(e: MissingServletRequestParameterException): ResponseEntity<ErrorResponse> {
        logger.error("Parâmetro não informado", e)
        return ResponseEntity(
            ErrorResponse("Parâmetro não informado"),
            org.springframework.http.HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleResourceNotFoundException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Erro interno", e)
        return ResponseEntity(
            ErrorResponse("Erro interno"),
            org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleResourceNotFoundException(e: DuplicateKeyException): ResponseEntity<ErrorResponse> {
        logger.error("Registro duplicado", e)
        return ResponseEntity(
            ErrorResponse("Registro já existe"),
            org.springframework.http.HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(PagamentoException::class)
    fun handleResourceNotFoundException(e: PagamentoException): ResponseEntity<ErrorResponse> {
        logger.error("Erro interno", e)
        return ResponseEntity(
            ErrorResponse(e.message!!),
            org.springframework.http.HttpStatus.BAD_REQUEST
        )
    }
}