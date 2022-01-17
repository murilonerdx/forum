package br.com.alura.forum.security;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository repository;

    public AuthenticationFilter(TokenService tokenService, UsuarioRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperarToken(httpServletRequest);
        boolean valid = tokenService.isTokenValid(token);
        if(valid){
            authenticateClient(token);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void authenticateClient(String token) {
        Long id = getIdUsuario(token);
        Usuario usuario = repository.findById(id).orElseThrow(()->new RuntimeException("ID not found"));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getPassword(), usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperarToken(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader("Authorization");
        if(authorization == null  || authorization.isEmpty() || !authorization.startsWith("Bearer ")){
            return null;
        }
        return authorization.substring(7, authorization.length());
    }

    public Long getIdUsuario(String token){
        return Long.parseLong(Jwts.parser().setSigningKey(token)
                .parseClaimsJws(token).getBody().getSubject());
    }


}
