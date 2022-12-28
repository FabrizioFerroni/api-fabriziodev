package app.railway.up.fabriziodevback.fabriziodevback.service;

import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyCard;
import app.railway.up.fabriziodevback.fabriziodevback.repository.quiensoyCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class quiensoyCardService {
    private static final Logger logger = LoggerFactory.getLogger(quiensoyCardService.class);

    @Autowired
    quiensoyCardRepository quiensoy;

    public List<quiensoyCard> listarTodos() {

        try {
            return quiensoy.findAllByOrderByIdDesc();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public quiensoyCard getById(Integer id) throws Exception {
        try {
            quiensoyCard quiensoyCard = quiensoy.findById(id).orElseThrow(() -> new Exception("No se encontró el registro con id: " + id));
            return quiensoyCard;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public List<quiensoyCard> getLastreg() throws Exception {
        try {
            return quiensoy.findLastRegister();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void guardar(quiensoyCard dto) {
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
