package app.railway.up.fabriziodevback.fabriziodevback.security.repository;

import app.railway.up.fabriziodevback.fabriziodevback.security.entity.Rol;
import app.railway.up.fabriziodevback.fabriziodevback.security.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByRolNombre(RolNombre rolNombre);
}
