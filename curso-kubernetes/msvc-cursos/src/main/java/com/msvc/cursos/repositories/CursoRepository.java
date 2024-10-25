package com.msvc.cursos.repositories;

import com.msvc.cursos.models.entity.Curso;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CursoRepository extends CrudRepository<Curso, Long> {

    //Modifying:Indica que la consulta modifica datos
    @Modifying
    @Query("DELETE FROM CursoUsuario cu WHERE cu.usuarioId = :id")
    public void eliminarCursoUsuario(@Param("id")Long id);

}
