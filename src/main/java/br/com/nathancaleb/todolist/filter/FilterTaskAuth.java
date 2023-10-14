package br.com.nathancaleb.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.nathancaleb.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component // Anotation usado para o Spring gerenciar esta classe
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var servletPath = request.getServletPath(); //Criado para validar a rota no caso /tasks/

                // se a rota for a /tasks/, executa todo o codigo.
                if(servletPath.startsWith("/tasks/")){
                        //PEGAR DADOS DE AUTENTICAÇÃO (USUARIO E SENHA)
                    var authorization = request.getHeader("Authorization");
                        //substring: no caso abaixo, vai pegar todos os caracteres da palavra Basic e depois com o Trim, remover todos os espaços sobrando
                    var authEncoded = authorization.substring("Basic".length()).trim();
                        //Decodificando nossa informação de autenticação que está em BASE64 - passando para um array
                    byte[] authDecode = Base64.getDecoder().decode(authEncoded);
                        //Decoficando o array, para os valores de usuario e senha
                    var authString = new String(authDecode);
                    
                    // Passando nosso usuario e senha para um array, usando o split para dividir ambos que estão juntos pelos ":"
                    String[] credentials = authString.split(":");

                    // atribuindo usuario e senha a respequitivas variaveis
                    String username = credentials[0];
                    String password = credentials[1];
                    
                    //VALIDAR USUARIO
                        var user = this.userRepository.findByUsername(username); //pego o meu repositorio, verifico se o usuario esta la dentro e atrelo a variavel User
                        // Se o usuario for Nulo, então apresento Erro ... se não, continuo com a rotina.
                        if(user == null){
                            response.sendError(401);
                        }else{
                            //VALIDAR SENHA
                            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()); //Através do Bcrypt, verifico se a senha esta correta, utilizo o toCharArray, porque o verify exige que o tipo de informação esteja em Array de caracteres BIT[]
                            // Se a verificação for verdadeira, seguimos com a aplicação ... se não, apresento erro
                            if(passwordVerify.verified){
                                request.setAttribute("idUser", user.getId()); //atribuindo o id do usuario inserido, dentro da nossa requisição para ser tratado no Task Controller
                                filterChain.doFilter(request, response);
                            }else{
                                response.sendError(401);
                            }       
                        }
                 }else{
                    filterChain.doFilter(request, response);
                 }
            }
}

/* 
UTILIZANDO FILTER
@Component // Anotation usado para o Spring gerenciar esta classe
public class FilterTaskAuth implements Filter{ //Realizamos a implementação do FILTER na nossa classe                                                //Utilizando o CTRL + . ele traz o metodo e importações por completo.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

            
        chain.doFilter(request, response);
    }
    
} */
