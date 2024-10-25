package com.msvc.kubernetes.usuarios.service;

import com.msvc.kubernetes.usuarios.models.entity.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);

    List<Usuario> listarPorIds(Iterable<Long> ids);

    Optional<Usuario> buscarPorEmail(String email);
}
