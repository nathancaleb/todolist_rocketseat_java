package br.com.nathancaleb.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Atribui Getters e Setters para todos os atributos da classe
@Entity(name = "tb_users") //Declarando que a nossa classe vai ser uma tabela de um banco de dados
public class UserModel {
    
    @Id //Através do Anotation ID informamos ao BD que essa será nossa chave unica
    @GeneratedValue(generator = "UUID") //Informamos ao SpringData JPA que irá gerenciar o geramento do ID e informamos o tipo de id que queremos, no caso o UUID.
    private UUID id;

    //@Column(name = "usuario") Informamos que o atributo username será uma coluno no BD com o nome "usuario"
    @Column(unique = true) //Atributo para informar ao BD que esta coluna sera de valores unicos
    private String username;
    private String name;
    private String password;

    @CreationTimestamp //Anotation que ira atrelar hora e data ao atributo createdAt, assim que o usuario for criado
    private LocalDateTime createdAt;

}
    
    
    
// ----------- Metodos getters e setters ----------- //
//getter: pegar valor de um atributo private de uma classe
//setter: setar valor de um atributo private de uma classe

    //set: cria uma classe pubica vazia que atribui o valor digitado a variavel private
    /* public void setUsername(String username) {
        this.username = username;
    } */

    //get: cria uma classe com o tipo equivalente a variavel private e retorna o valor dela
    /* public String getUsername() {
        return username;
    } */

    /* // atribuindo variavel name
    public void setName(String name) {
        this.name = name;
    }

    // retornando variavel name
    public String getName() {
        return name;
    }

    // atribuindo variavel password
    public void setPassword(String password) {
        this.password = password;
    }

    // retornando variavel password
    public String getPassword() {
        return password;
    }
*/

