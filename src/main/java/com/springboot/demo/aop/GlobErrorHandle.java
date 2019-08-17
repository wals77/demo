package com.springboot.demo.aop;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;


@RestControllerAdvice
public class GlobErrorHandle {

    /**
     * 对方法里的注解校验
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleValidError(ConstraintViolationException ex){
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        if (!CollectionUtils.isEmpty(constraintViolations)){
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation violation: constraintViolations){
                sb.append(violation.getMessage()).append(",");
            }
            String errorMessage = sb.toString();
            if (errorMessage.length() >1){
                errorMessage = errorMessage.substring(0, errorMessage.length()-1);
            }
            return new ResponseEntity(errorMessage, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 对Bean里面的注解校验
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity method(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(allErrors)){
            StringBuilder sb = new StringBuilder();
            for (ObjectError error: allErrors){
                sb.append(error.getDefaultMessage()).append(",");
            }
            String errorMessage = sb.toString();
            if (errorMessage.length() >1){
                errorMessage = errorMessage.substring(0, errorMessage.length()-1);
            }
            return new ResponseEntity(errorMessage, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
