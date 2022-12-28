package app.railway.up.fabriziodevback.fabriziodevback.repository;

import app.railway.up.fabriziodevback.fabriziodevback.entity.rrss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface rrssRepository extends JpaRepository<rrss, Integer> {
    List<rrss> findAllByOrderByIdDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM rrss ORDER BY id DESC LIMIT 1")

    List<rrss> findLastRegister();
}
