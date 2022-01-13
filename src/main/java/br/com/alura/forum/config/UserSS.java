package br.com.alura.forum.config;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSS implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Usuario byEmail = repository.findByEmail(s).orElseThrow(()-> new UsernameNotFoundException("e-mail não encontrado"));
        return byEmail;
    }
}
