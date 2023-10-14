package br.com.nathancaleb.todolist.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;


@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired //Anotation pro Spring gerar todo ciclo de vida ??? confuso!
    private IUserRepository userRepository; //trago os metodos do JPARepository - dendo do IUserRepository

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){ //informamos o tipo ResponseEntity, assim conseguimos dar mais de um tipo de resposta.
        var user = this.userRepository.findByUsername(userModel.getUsername()); //Conseguimos agora usar o findBy aqui
        
        if(user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario Já Existe"); // Retornamos o ResponseEntity com o BAD REQUEST para o usuario
        }

        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        //chamamos o Bcrypt com a opção withDefaults -> (varios comando do Bcrypt) ... usando o hashToString ... (cost: padrão 12, declaramos qual queremos alterar no caso o nosso userModel.getPassword e usamos o toCharArray(), poís é de uso obrigado transformarmos nossa chave em um array)

        userModel.setPassword(passwordHashred); //Depois, setamos no nosso password a senha criptografada

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated); // Retornamos o ResponseEntity com o CREATED para o usuario
        //System.out.println(userModel.getUsername()); printar na tela - codigo antigo
    }
}

/*
     * String(texto)
     * Interger (Numeros inteiros)
     * Double (números 0.000)
     * Float (números 0.000)
     * char  (A C)
     * Date (data)
     * void (sem retorno)
    */