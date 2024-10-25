package com.msvc.cursos.services;

import com.msvc.cursos.models.Usuario;
import com.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);

    // Este endpoint te muestra el detalle completo
    Optional<Curso> porIdConUsuarios(Long id);

    void eliminarCursoUsuarioPorId(Long id);

    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);
    //Desasinar del curso
    Optional<Usuario> desasignarUsuario(Usuario usuario, Long id);
}
