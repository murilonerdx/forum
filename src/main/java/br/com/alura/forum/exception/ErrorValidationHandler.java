package br.com.alura.forum.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorValidationHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorFieldValidation>> handler(MethodArgumentNotValidException e){
        List<ErrorFieldValidation> errors = new ArrayList<>();

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(x-> {
            String mensagem = messageSource.getMessage(x, LocaleContextHolder.getLocale());
            ErrorFieldValidation error = new ErrorFieldValidation(x.getField(), mensagem);
            errors.add(error);
        });

        return ResponseEntity.ok().body(errors);
    }
}
