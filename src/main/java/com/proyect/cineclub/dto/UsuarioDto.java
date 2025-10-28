package com.proyect.cineclub.dto;

import com.proyect.cineclub.entity.Usuario;
import com.proyect.cineclub.security.Rol;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioDto {
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres.")
    private String username;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    private String apellido;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String contraseña;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public static Usuario fromUsuarioDto(UsuarioDto dto){
        Usuario usuario =  new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setApellido(dto.getApellido());
        usuario.setContraseña(dto.getContraseña());
        usuario.setRol(Rol.USER);
        return usuario;
    }
}
