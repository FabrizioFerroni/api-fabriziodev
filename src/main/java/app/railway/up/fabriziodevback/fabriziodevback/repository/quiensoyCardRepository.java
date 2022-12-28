package app.railway.up.fabriziodevback.fabriziodevback.repository;

import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface quiensoyCardRepository extends JpaRepository<quiensoyCard, Integer> {
    List<quiensoyCard> findAllByOrderByIdDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM quiensoy_card ORDER BY id DESC LIMIT 1")
    List<quiensoyCard> findLastRegister();
}
