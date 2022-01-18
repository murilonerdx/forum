package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.TokenDTO;
import br.com.alura.forum.modelo.LoginForm;
import br.com.alura.forum.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping()
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginForm login){
        UsernamePasswordAuthenticationToken dadosLogin = login.converter();

        try{
            Authentication authentication = authManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);

            return ResponseEntity.ok().body(new TokenDTO(token, "Bearer"));
        }catch(AuthenticationException e ){
            return ResponseEntity.badRequest().build();
        }
    }

}
