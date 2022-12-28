package app.railway.up.fabriziodevback.fabriziodevback.repository;

import app.railway.up.fabriziodevback.fabriziodevback.entity.inicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface inicioRepository extends JpaRepository<inicio, Integer> {

    List<inicio> findAllByOrderByIdDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM inicio ORDER BY id DESC LIMIT 1")

    List<inicio> findLastRegister();
}
