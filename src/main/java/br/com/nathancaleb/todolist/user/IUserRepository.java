package br.com.nathancaleb.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
// import java.util.List;


public interface IUserRepository extends JpaRepository<UserModel, UUID>{ //Extendendo tudo dentro de JPARepository (metodos, anotations) para a interface IUserRepository
    UserModel findByUsername(String username); //adicionamos a nossa interface o findby para ser usado pelo nosso controller
}
