package app.railway.up.fabriziodevback.fabriziodevback.security.service;

import app.railway.up.fabriziodevback.fabriziodevback.security.entity.Rol;
import app.railway.up.fabriziodevback.fabriziodevback.security.enums.RolNombre;
import app.railway.up.fabriziodevback.fabriziodevback.security.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RolService {
    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepository.findByRolNombre(rolNombre);
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
}
