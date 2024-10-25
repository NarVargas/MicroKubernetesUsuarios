package com.msvc.kubernetes.usuarios.service;

import com.msvc.kubernetes.usuarios.clients.CursoClientRest;
import com.msvc.kubernetes.usuarios.models.entity.Usuario;
import com.msvc.kubernetes.usuarios.repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuariosRepository repository;

    @Autowired
    private CursoClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) repository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porId(Long id) {
        return repository.findById(id);
    }


    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        // Aqui aprovechamos el eliminar usuario
        // para eliminar el del cliente tambien y no hayan datos ambiguos
        repository.deleteById(id);
        client.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ArrayList<Usuario> listarPorIds(Iterable<Long> ids) {
        return (ArrayList<Usuario>) repository.findAllById(ids);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }
}
