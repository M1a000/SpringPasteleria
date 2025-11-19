// src/main/java/com/PasteleriaMilSabores/security/services/UserDetailsImpl.java

package com.PasteleriaMilSabores.security.services;

import com.PasteleriaMilSabores.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String nombreUsuario;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String nombreUsuario, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.authorities = authorities;
    }

    // Método estático para construir UserDetailsImpl a partir de tu Entidad Usuario
    public static UserDetailsImpl build(Usuario usuario) {
        // Convierte el String de Rol a un objeto GrantedAuthority
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(usuario.getRol())); 

        return new UserDetailsImpl(
            usuario.getId(), 
            usuario.getEmail(),
            usuario.getNombre(),
            usuario.getPassword(), 
            authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Getters customizados
    public Long getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }

    // Implementaciones de UserDetails
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return email; } // Spring Security usa getUsername() como el campo de login (email en este caso)
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}