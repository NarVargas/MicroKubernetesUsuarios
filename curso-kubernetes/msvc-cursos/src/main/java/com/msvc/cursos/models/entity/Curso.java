package com.msvc.cursos.models.entity;

import com.msvc.cursos.models.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nombre;

    // Un curso puede tener muchos alumnos
    // CascadeType.ALL: Propaga todas las operaciones realizadas en la entidad padre a las entidades hijas
    // (eliminar, actualizar, etc.)

    //orphanRemoval: Si una entidad hija es eliminada de la colección del padre
    // también se eliminará automáticamente de la base de datos.

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curso_id")
    private List<CursoUsuario> cursoUsuarios;


    // Transient: será ignorado por JPA cuando se guarde o actualice la entidad en la base de datos.
    @Transient
    private List<Usuario> usuarios;

    public Curso() {
        cursoUsuarios = new ArrayList<>();
        usuarios = new ArrayList<>();
    }


    public void addCursoUsuario(CursoUsuario cursoUsuario)
    {
        cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario)
    {
        cursoUsuarios.remove(cursoUsuario);
    }

    public void getCursoUsuario(Usuario usuario){
        usuarios.add(usuario);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<CursoUsuario> getCursoUsuarios() {
        return cursoUsuarios;
    }

    public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios) {
        this.cursoUsuarios = cursoUsuarios;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
