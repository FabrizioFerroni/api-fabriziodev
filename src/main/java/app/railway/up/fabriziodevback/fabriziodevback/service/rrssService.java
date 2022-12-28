package app.railway.up.fabriziodevback.fabriziodevback.service;

import app.railway.up.fabriziodevback.fabriziodevback.entity.rrss;
import app.railway.up.fabriziodevback.fabriziodevback.repository.rrssRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class rrssService {

    private static final Logger logger = LoggerFactory.getLogger(maintenanceService.class);

    @Autowired
    rrssRepository rrssRepo;

    public List<rrss> listarTodos() {
        try {
            return rrssRepo.findAllByOrderByIdDesc();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public rrss getById(Integer id) throws Exception {
        try {
            rrss misredes = rrssRepo.findById(id).orElseThrow(() -> new Exception("No se encontró el registro con id: " + id));
            return misredes;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public List<rrss> getLastreg() throws Exception {
        try {
            return rrssRepo.findLastRegister();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void guardar(rrss dto) {
        try {
            rrssRepo.save(dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void borrar(Integer id) {
        try {
            rrssRepo.deleteById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean existsById(int id) {
        try {
            return rrssRepo.existsById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
