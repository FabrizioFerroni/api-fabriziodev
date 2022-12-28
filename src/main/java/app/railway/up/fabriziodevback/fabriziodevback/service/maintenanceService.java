package app.railway.up.fabriziodevback.fabriziodevback.service;

import app.railway.up.fabriziodevback.fabriziodevback.entity.maintenance;
import app.railway.up.fabriziodevback.fabriziodevback.repository.maintenanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class maintenanceService {
    private static final Logger logger = LoggerFactory.getLogger(maintenanceService.class);

    @Autowired
    maintenanceRepository main;

    public List<maintenance> listarTodos() {
        try {
            return main.findAllByOrderByIdDesc();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public maintenance getById(Integer id) throws Exception {
        try {
            maintenance maint = main.findById(id).orElseThrow(() -> new Exception("No se encontró el registro con id: " + id));
            return maint;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public List<maintenance> getLastreg() throws Exception {
        try {
            return main.findLastRegister();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void guardar(maintenance dto) {
        try {
            main.save(dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean existsById(int id) {
        try {
            return main.existsById(id);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

}

