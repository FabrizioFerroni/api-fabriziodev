package app.railway.up.fabriziodevback.fabriziodevback.service;

import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyGeneral;
import app.railway.up.fabriziodevback.fabriziodevback.repository.quiensoyGeneralRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class quiensoyGeneralService {
    private static final Logger logger = LoggerFactory.getLogger(quiensoyGeneralService.class);

    @Autowired
    quiensoyGeneralRepository quiensoy;

    public List<quiensoyGeneral> listarTodos() {
        try{
        return quiensoy.findAllByOrderByIdDesc();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public quiensoyGeneral getById(Integer id) throws Exception {
        try{
        quiensoyGeneral quiensoyGen = quiensoy.findById(id).orElseThrow(() -> new Exception("No se encontró el registro con id: " + id));
        return quiensoyGen;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public List<quiensoyGeneral> getLastreg() throws Exception {
        try{
        return quiensoy.findLastRegister();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void guardar(quiensoyGeneral dto) {
        try{
        quiensoy.save(dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void borrar(Integer id) {
        try{
        quiensoy.deleteById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean existsById(int id) {
        try{
        return quiensoy.existsById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
