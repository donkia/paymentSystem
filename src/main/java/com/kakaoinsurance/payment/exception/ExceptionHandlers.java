package com.kakaoinsurance.payment.exception;

import com.kakaoinsurance.payment.web.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<?> customException(CustomException ex){
        log.info(ex.toString());
        return new ResponseEntity<>(new ErrorDto(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage()), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<?> illegalArgumentException(Exception ex){
        log.info("IllegalArgumentException : {}" , ex.getMessage());
        return new ResponseEntity<>(new ErrorDto(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<?> exception(Exception ex){
        log.info(ex.toString());
        return new ResponseEntity<>(new ErrorDto(500, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
