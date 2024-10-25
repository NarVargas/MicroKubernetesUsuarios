package com.msvc.kubernetes.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// La url se elimina al integrar spring cloud con kubernetes
@FeignClient(name = "msvc-cursos", url = "localhost:8002")
public interface CursoClientRest {

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    void eliminarCursoUsuarioPorId(@PathVariable Long id);
}
