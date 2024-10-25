package com.msvc.kubernetes.usuarios.repositories;

import com.msvc.kubernetes.usuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuariosRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
