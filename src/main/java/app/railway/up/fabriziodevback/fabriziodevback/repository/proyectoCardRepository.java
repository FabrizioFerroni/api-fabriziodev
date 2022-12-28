package app.railway.up.fabriziodevback.fabriziodevback.repository;

import app.railway.up.fabriziodevback.fabriziodevback.entity.proyectoCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface proyectoCardRepository extends JpaRepository<proyectoCard, Integer> {
    List<proyectoCard> findAllByOrderByIdDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM proyecto_card ORDER BY id DESC LIMIT 1")
    List<proyectoCard> findLastRegister();
}
