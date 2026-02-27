package com.chinmaysinghmodak.invoicing.exception

//import com.chinmaysinghmodak.chefio_backend.dto.ApiError
import com.chinmaysinghmodak.chefio_backend.exception.ApiException
import com.chinmaysinghmodak.invoicing.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
class GlobalExceptionHandler(
//    private val errorLogService: LoggerService
) {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(
        ex: ApiException, request: WebRequest
    ): ResponseEntity<ApiResponse<Nothing>> {

//        val traceId = errorLogService.log(
//            message = ex.message, ex = ex, request = request, status = ex.status
//        )

        return ResponseEntity.status(ex.status).body(
            ApiResponse(
                success = false, error = ex.message, data = null
            )
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMissingRequestBody(
        ex: HttpMessageNotReadableException,
        request: WebRequest
    ): ResponseEntity<ApiResponse< String>> {
//        val traceId = errorLogService.log(
//            message = "Missing or invalid request body",
//            ex = ex,
//            request = request,
//            status = HttpStatus.BAD_REQUEST
//        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse(
                success = false,
                error = ex.message,
                data = null
            )
        )
    }


    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParams(
        ex: MissingServletRequestParameterException,
        request: WebRequest
    ): ResponseEntity<ApiResponse< String>> {
//        val traceId = errorLogService.log(
//            message = "Missing request parameter",
//            ex = ex,
//            request = request,
//            status = HttpStatus.BAD_REQUEST
//        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse(
                success = false,
                error = ex.message,
                data = null
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ApiResponse< Map<String, Any>>> {

//        val traceId = errorLogService.log(
//            message = "Validation failed for request parameters",
//            ex = ex,
//            request = request,
//            status = HttpStatus.BAD_REQUEST
//        )

        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        return ResponseEntity.badRequest().body(
            ApiResponse(
                success = false,
                error = errors,
                data = null
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnhandledException(
        ex: Exception, request: WebRequest
    ): ResponseEntity<ApiResponse< String>> {

//        val traceId = errorLogService.log(
//            message = ex.message ?: "Unexpected error",
//            ex = ex,
//            request = request,
//            status = HttpStatus.INTERNAL_SERVER_ERROR
//        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiResponse(
                success = false,
                error = ex.message,
                data = null,
            )
        )
    }

    @ExceptionHandler(value = [NoHandlerFoundException::class])
    fun handle404(
        ex: NoHandlerFoundException, request: WebRequest
    ): ResponseEntity<ApiResponse<String>> {

//
//        val traceId = errorLogService.log(
//            message = "API endpoint not found", ex = ex, request = request, status = HttpStatus.NOT_FOUND
//        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiResponse(
                success = false, error = ex.message, data = null
            )
        )

    }

}
