package com.msvc.cursos.controllers;

import com.msvc.cursos.models.Usuario;
import com.msvc.cursos.models.entity.Curso;
import com.msvc.cursos.services.CursoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> detalle(@PathVariable Long id) {
        Optional<Curso> optional = service.porIdConUsuarios(id);
        //service.porId();

        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {

        if(result.hasErrors()) {
            return validar(result);
        }

        Curso cursoBd = service.guardar(curso);

        return ResponseEntity.status(HttpStatus.CREATED).body(cursoBd);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {

        if(result.hasErrors()) {
            return validar(result);
        }

        Optional<Curso> optional = service.porId(id);

        if (optional.isPresent()) {
            Curso cursoBd = optional.get();
            cursoBd.setNombre(curso.getNombre());

            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoBd));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> optional = service.porId(id);

        if (optional.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@Valid @RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOptional;
        try {
          usuarioOptional = service.asignarUsuario(usuario, cursoId);

        }
        catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("Error", "No existe el usuario o error con la comunicacion:"+ e.getMessage()));
        }
        if(usuarioOptional.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOptional;
        try {
            usuarioOptional = service.crearUsuario(usuario, cursoId);

        }
        catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("Error",
                                    "No se ha creado el usuario o error con la comunicacion:"+ e.getMessage()));
        }
        if(usuarioOptional.isPresent())
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOptional.get());

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@Valid @RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioOptional;
        try {
            usuarioOptional = service.desasignarUsuario(usuario, cursoId);

        }
        catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("Error",
                                    "No se existe el usuario por el id o error con la comunicacion:"+ e.getMessage()));
        }
        if(usuarioOptional.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(Long id)
    {

        service.eliminarCursoUsuarioPorId(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections
                .singletonMap("Mensaje", "Usuario eliminado correctamente"));
    }

    // Validacion de parametros
    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errores.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errores);
    }
}
