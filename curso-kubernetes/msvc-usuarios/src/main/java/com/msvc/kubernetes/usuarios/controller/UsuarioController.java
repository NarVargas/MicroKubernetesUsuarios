package com.msvc.kubernetes.usuarios.controller;

import com.msvc.kubernetes.usuarios.models.entity.Usuario;
import com.msvc.kubernetes.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    //PathVariable indica la variable que recibe por el GetMapping, al tener el mismo nombre la recoge automaticamente
    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);

        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        // Generamos el BindingResult para posibles errores, a traves del
        // getFieldErrors recibimos todos los campos que han dado error

        if(result.hasErrors()) {
            return validar(result);
        }

        if(!usuario.getEmail().isEmpty() && service.buscarPorEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Collections.
                            singletonMap("Mensaje", "Ya existe un usuario con ese correo"));
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){

        if(result.hasErrors()) {
            return validar(result);
        }

        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario2 = usuarioOptional.get();

            // Si el correo es el mismo que ya tenia no entra por aqui, y si es otro que no exista tampoco
            if(!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuario2.getEmail())
                    && service.buscarPorEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Collections.
                                singletonMap("Mensaje", "Ya existe un usuario con ese correo"));
            }

            usuario2.setNombre(usuario.getNombre());
            usuario2.setEmail(usuario.getEmail());
            usuario2.setPassword(usuario.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario2));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/usuarios-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids)
    {
        return ResponseEntity.ok(service.listarPorIds(ids));
    }
    // Validacion de parametros
    private ResponseEntity<Map<String, String>> validar(@RequestParam BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errores.put(error.getField(), "El campo " + error.getField() + " "+ error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errores);
    }
}
