package br.com.alura.forum.security;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication){
        Usuario sub = (Usuario) authentication.getPrincipal();

        Date dtInitialCreate = new Date();
        Date expirationFinalCreate = new Date(dtInitialCreate.getTime() + expiration);

        return Jwts.builder()
                .setIssuer("API do Forum")
                .setSubject(sub.getEmail())
                .setIssuer(String.valueOf(dtInitialCreate))
                .setExpiration(expirationFinalCreate)
                .signWith(SignatureAlgorithm.ES256, secret)
                .compact();
    }
}
