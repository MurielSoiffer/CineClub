package com.proyect.cineclub.service;

import com.proyect.cineclub.entity.Usuario;
import com.proyect.cineclub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario save(Usuario usuario){
        Usuario usuario1 = usuario;
        String encryptedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario1.setContraseña(encryptedPassword);
        return usuarioRepository.save(usuario1);
    }

    @Transactional
    public Usuario updateById(Usuario request, Long id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if(usuarioExistente.isEmpty()) {
            // .orElseThrow(() -> new RuntimeException("Usuario no encontrada"));
        }
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        usuarioExistente.get().setContraseña(encryptedPassword);

        return usuarioRepository.save(usuarioExistente.get());
    }

    public Page<Usuario> getAll(Pageable pageable){return usuarioRepository.findAll(pageable);}

    public Optional<Usuario> getById(Long id){return usuarioRepository.findById(id);}

    public Usuario getReferenceById(Long id){return usuarioRepository.getReferenceById(id);}

    public void deleteById(Long id){usuarioRepository.deleteById(id);}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if(usuario == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("user not found");
        }
        return usuario;
    }


}
