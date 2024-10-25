package com.msvc.cursos.services;

import com.msvc.cursos.clients.UsuarioClientRest;
import com.msvc.cursos.models.Usuario;
import com.msvc.cursos.models.entity.Curso;
import com.msvc.cursos.models.entity.CursoUsuario;
import com.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository repository;

    @Autowired
    private UsuarioClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    //Cuando es solo consulta se recomienda readOnly
    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> optional = repository.findById(id);

        if(optional.isPresent()) {
            Curso curso = optional.get();
            // Si no contiene ningun usuario en el curso
            if(!curso.getCursoUsuarios().isEmpty()){
                //Todos los usuarios
                List<Long> ids = new ArrayList<>();
                // Guardamos el id de cada usuario
                for (CursoUsuario usuario : curso.getCursoUsuarios()) {
                    ids.add(usuario.getUsuarioId());
                }

                List<Usuario> usuarios = client.obtenerAlumnosPorCurso(ids);

                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return repository.findById(id);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        repository.eliminarCursoUsuario(id);
    }

    //El transactional es algo sale mal en medio de una operación, se puede revertir
    // toodo lo que ya se haya hecho

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {

        //Buscamos el id del curso
        Optional<Curso> cursoOptional = repository.findById(cursoId);

        //Si existe el curso
        if (cursoOptional.isPresent()) {
            // Buscamos si existe el usuario en ese curso
            Usuario usuarioMs = client.detalle(usuario.getId());

            // En caso de que no haya error obtenemos el curso
            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMs.getId());

            //Lo añadimos al curso el usuario
            curso.addCursoUsuario(cursoUsuario);

            repository.save(curso);

            return Optional.of(usuarioMs);
        }
        return Optional.empty();
    }


    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoOptional = repository.findById(cursoId);

        //Si existe el curso
        if (cursoOptional.isPresent()) {
            // Creamos usuario nuevo
            Usuario usuarioNuevo = client.crear(usuario);

            // En caso de que no haya error obtenemos el curso
            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevo.getId());

            //Lo añadimos al curso el usuario
            curso.addCursoUsuario(cursoUsuario);

            repository.save(curso);

            return Optional.of(usuarioNuevo);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> desasignarUsuario(Usuario usuario, Long cursoId) {

        Optional<Curso> cursoOptional = repository.findById(cursoId);

        //Si existe el curso
        if (cursoOptional.isPresent()) {
            // Buscamos si existe en el curso
            Usuario usuarioMs = client.detalle(usuario.getId());

            // En caso de que no haya error obtenemos el curso
            Curso curso = cursoOptional.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMs.getId());

            //Lo añadimos al curso el usuario
            curso.removeCursoUsuario(cursoUsuario);

            repository.save(curso);

            return Optional.of(usuarioMs);
        }

        return Optional.empty();
    }
}
