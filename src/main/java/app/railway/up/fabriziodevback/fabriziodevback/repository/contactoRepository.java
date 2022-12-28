package app.railway.up.fabriziodevback.fabriziodevback.repository;

import app.railway.up.fabriziodevback.fabriziodevback.entity.inicio;
import org.springframework.data.jpa.repository.JpaRepository;
import app.railway.up.fabriziodevback.fabriziodevback.entity.contacto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface contactoRepository extends JpaRepository<contacto, Integer> {
    List<contacto> findAllByOrderByIdDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM contacto ORDER BY id DESC LIMIT 1")

    List<contacto> findLastRegister();
}
