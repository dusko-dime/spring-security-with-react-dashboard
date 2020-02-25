package com.example.polls.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonController {
    public static Log log = LogFactory.getLog(CommonController.class);

    @ExceptionHandler(RestException.class)
    public final ResponseEntity<Object> handleRestException(RestException e){
        if (e.getData()!=null && e.getData() instanceof Exception){
            logError(e,log);
            return new ResponseEntity<>(null,e.getStatus());
        }
        return new ResponseEntity<>(e.getData(),e.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(Exception e){
        logError(e,log);
        return new ResponseEntity<>("Not authorized to call it", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e){
        logError(e,log);
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logError(Exception e, Log log){
        log.error(e);
        for (StackTraceElement element : e.getStackTrace())
            log.error(element.toString());
    }
}
