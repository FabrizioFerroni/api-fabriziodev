package app.railway.up.fabriziodevback.fabriziodevback.repository;

import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface quiensoyServiceRepository extends JpaRepository<quiensoyService, Integer> {

    List<quiensoyService> findAllByOrderByIdDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM quiensoy_service ORDER BY id DESC LIMIT 1")

    List<quiensoyService> findLastRegister();

}
