package app.railway.up.fabriziodevback.fabriziodevback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.railway.up.fabriziodevback.fabriziodevback.entity.proyectoGeneral;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface proyectoGeneralRepository extends JpaRepository<proyectoGeneral, Integer> {
    List<proyectoGeneral> findAllByOrderByIdDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM proyecto_general ORDER BY id DESC LIMIT 1")
    List<proyectoGeneral> findLastRegister();
}
