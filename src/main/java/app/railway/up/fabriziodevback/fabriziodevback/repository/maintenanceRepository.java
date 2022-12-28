package app.railway.up.fabriziodevback.fabriziodevback.repository;

import app.railway.up.fabriziodevback.fabriziodevback.entity.maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface maintenanceRepository extends JpaRepository<maintenance, Integer> {
    List<maintenance> findAllByOrderByIdDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM maintenance ORDER BY id DESC LIMIT 1")
    List<maintenance> findLastRegister();
}
