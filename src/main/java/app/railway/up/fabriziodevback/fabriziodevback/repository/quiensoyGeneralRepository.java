package app.railway.up.fabriziodevback.fabriziodevback.repository;

import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.*;

import java.util.List;

@Repository
public interface quiensoyGeneralRepository extends JpaRepository<quiensoyGeneral, Integer> {

    List<quiensoyGeneral> findAllByOrderByIdDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM quiensoy_general ORDER BY id DESC LIMIT 1")
    List<quiensoyGeneral> findLastRegister();
}
