package app.railway.up.fabriziodevback.fabriziodevback.service;

import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyService;
import app.railway.up.fabriziodevback.fabriziodevback.repository.quiensoyServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class quiensoyServiceService {
    private static final Logger logger = LoggerFactory.getLogger(maintenanceService.class);

    @Autowired
    quiensoyServiceRepository quiensoy;

    public List<quiensoyService> listarTodos() {
        try {
            return quiensoy.findAllByOrderByIdDesc();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public quiensoyService getById(Integer id) throws Exception {
        try {
            quiensoyService quiensoyServ = quiensoy.findById(id).orElseThrow(() -> new Exception("No se encontró el registro con id: " + id));
            return quiensoyServ;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public List<quiensoyService> getLastreg() throws Exception {
        try {
            return quiensoy.findLastRegister();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void guardar(quiensoyService dto) {
        try {
            quiensoy.save(dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void borrar(Integer id) {
        try {
            quiensoy.deleteById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean existsById(int id) {
        try {
            return quiensoy.existsById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
