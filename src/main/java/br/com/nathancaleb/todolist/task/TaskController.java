package br.com.nathancaleb.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nathancaleb.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController //indica para o framework que se trata de um controlador Rest, voltado para o desenvolvimento de aplicações web Restful (METODOS HTML)
@RequestMapping("/tasks") //Definindo endereço da pagina
public class TaskController {
    
    @Autowired //Usado para o String gerenciar a instancia do nosso repositorio
    private ITaskRepository taskRepository; //definimos nosso repositorio

    @PostMapping("/") //Determina que o método aceitará requisições HTTP do tipo POST
    //RequestBody: informa da onde vem a informação, no caso do corpo da requisição.
    //Usamos o HttpServletRequest para trazer na requisição o nosso id de usuario
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var idUser = request.getAttribute("idUser"); //Pegando o atribudo ID do usuario da nossa requisição e atrelando a variavel
        taskModel.setIdUser((UUID) idUser); //Setando a Taks o Id do usuario da nossa requisição.
        
        // VALIDAÇÃO DE DATA
        var currentDate = LocalDateTime.now(); //variavel com a data atual
        //se data atual for depois da data de inicio/termino, imprime erro
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio/termino deve ser maior do que a data atual.");
        }else if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){ //mas se a data de inicio for maior que a de termino.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data de termino.");
        }

        //SALVANDO A TASK
        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task); //retornamos o item salvo
    }

    @GetMapping("/")
    // Impressão da lista de tasks
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}") //<-- Spring Boot vai substituir o id nas chaves pelo nosso id dentro do Public UPDATE
    //PathVariable é o caminho da barra de endereço referente ao Id de algum item, no caso, o ID da taks ex: http://localhost:8080/tasks/cb34234-fdfew3-fsdfew <- esta ultima informação
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        // var idUser = request.getAttribute("idUser");
        
        var task = this.taskRepository.findById(id).orElse(null);
        
        if(task == null){ //Caso o usuario busque uma tarefa não existente, retornamos o erro de inexistencia
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada.");
        }

        var idUser = request.getAttribute("idUser");
        // PONTO DE EXCLAMAÇÃO É USADO PARA CASO NEGATIVO (FALSE) - NO CASO ABAIXO, SE A ID DA DE USUARIO DA TASK FOR DIFERENTE DO ID DO USUARIO QUE ESTA AUTENTICADO
        if(!task.getIdUser().equals(idUser)){ //Caso o usuario tente editar uma task que não foi criada pelo seu usuario, retornamos não autorizado.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario sem permissão para auterar esta tarefa.");
        }

        Utils.copyNonNullProperties(taskModel, task);
        
        // taskModel.setIdUser((UUID) idUser); //setando o id do usuario
        // taskModel.setId(id); //setando o id da task
        var taskUpdated = this.taskRepository.save(task); //retornando o que esta salvo
        return ResponseEntity.ok().body(taskUpdated);
    }

}
