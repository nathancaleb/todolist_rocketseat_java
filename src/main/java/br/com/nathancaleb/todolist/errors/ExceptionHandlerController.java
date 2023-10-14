package br.com.nathancaleb.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//CONTROLLERADVICE: Anotation do Spring - Usado para definir classes globais no momento de tratamento de exceções
//toda exceção que tiver na aplicação vai passar pelo ControllerAdvice
@ControllerAdvice
public class ExceptionHandlerController {
    
    //ExceptionHandler: Para informar que o nosso metodo sera exatamente para este tipo de exceção
    @ExceptionHandler(HttpMessageNotReadableException.class) //Passamos o tipo da exceção dentro do paranteses
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){ //Passamos o retorno da nossa mensagem de erro
        //getMostSpecificCause, não repassamos o objeto, apenas a mensagem de erro.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage()); //retornamos a mensagem de erro
    }
}
