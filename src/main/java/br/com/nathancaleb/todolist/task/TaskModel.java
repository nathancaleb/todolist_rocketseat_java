/** 
 O que a tarefa precisa ter
 * 
 * id
 * usuario (quem vai cadastrar a task) (id_usuario)
 * descrição
 * titulo
 * data de inicio da tarefa
 * data de termino da tarefa
 * prioridade
 * 
**/

package br.com.nathancaleb.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Getters e Setters
@Entity(name  = "tb_tasks") // Criando uma entidade para ser usuado como BD de nome tb_tasks
public class TaskModel {
    @Id // define como chave primaria
    @GeneratedValue(generator = "UUID") // Para gerar o ID automaticamente atraves do Spring
    private UUID id; //id da tarefa
    private String description;

    @Column(length = 50) // Definindo nesta coluna TITLE que o tamanho maximo de caracteres é até 50 (var char max: 255 caracteres)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser; // id do usuario

    @CreationTimestamp  // Para gravar data e hora quando a task for criada
    private LocalDateTime createdAt;

    //Abaixo passamos a exceção para ser tratada pelo peloi Exception utilizando throws Exception
    public void setTitle(String title) throws Exception{ //Aqui será verificado se o titulo esta dentro do limite de caracteres, caso não esteja, retornamos erro.
        if(title.length() > 50){
            throw new Exception("O campo title deve conter no máximo 50 caracteres");
        }
        this.title = title;
    }
}
