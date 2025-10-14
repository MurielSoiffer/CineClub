package com.proyect.cineclub.controller;

import com.proyect.cineclub.entity.Usuario;
import com.proyect.cineclub.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping()
    public List<Usuario> getUsuarios(@PageableDefault(size = 5, sort = "id") Pageable pageable){
        return usuarioService.getAll(pageable).getContent();
    }
    @PostMapping(path = "/post")
    public Usuario save(@RequestBody @Valid Usuario usuario){
        Usuario saveUsuario = this.usuarioService.save(usuario);
        return usuario;
    }
    @PutMapping(path = "/{id}")
    public ResponseEntity<Usuario> updateById(@RequestBody Usuario request, @PathVariable("id") long id){
        return ResponseEntity.ok(this.usuarioService.updateById(request, id));
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable("id") Long id){
        Optional<Usuario> usuarioOptional = this.usuarioService.getById(id);
        if (usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping(path = "/{id}")
    public void deleteUsuarioById(@PathVariable("id") Long id){
        this.usuarioService.deleteById(id);
    }

}
