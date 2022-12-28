package app.railway.up.fabriziodevback.fabriziodevback.service;

import app.railway.up.fabriziodevback.fabriziodevback.entity.proyectoGeneral;
import app.railway.up.fabriziodevback.fabriziodevback.repository.proyectoGeneralRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class proyectoGeneralService {
    private static final Logger logger = LoggerFactory.getLogger(proyectoGeneralService.class);

    @Autowired
    proyectoGeneralRepository prGenRepository;

    public List<proyectoGeneral> listarTodos() {
        try {
            return prGenRepository.findAllByOrderByIdDesc();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public proyectoGeneral getprgenById(Integer id) throws Exception {
        try {
            proyectoGeneral proyGen = prGenRepository.findById(id).orElseThrow(() -> new Exception("No se encontró el registro con id: " + id));
            return proyGen;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public List<proyectoGeneral> getLastprgen() throws Exception {
        try {
            return prGenRepository.findLastRegister();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void guardar(proyectoGeneral dto) {
        try {
            prGenRepository.save(dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void borrar(Integer id) {
        try {
            prGenRepository.deleteById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean existsById(int id) {
        try {
            return prGenRepository.existsById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
