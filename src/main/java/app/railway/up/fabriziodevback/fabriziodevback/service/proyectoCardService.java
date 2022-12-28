package app.railway.up.fabriziodevback.fabriziodevback.service;

import app.railway.up.fabriziodevback.fabriziodevback.entity.proyectoCard;
import app.railway.up.fabriziodevback.fabriziodevback.repository.proyectoCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class proyectoCardService {
    private static final Logger logger = LoggerFactory.getLogger(proyectoCardService.class);

    @Autowired
    proyectoCardRepository proyectoCardRepository;

    public List<proyectoCard> listarTodos() {
        try {
            return proyectoCardRepository.findAllByOrderByIdDesc();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public proyectoCard getprcardById(Integer id) throws Exception {
        try {
            proyectoCard proyCard = proyectoCardRepository.findById(id).orElseThrow(() -> new Exception("No se encontró el registro con id: " + id));
            return proyCard;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public List<proyectoCard> getLastprcard() throws Exception {
        try {
            return proyectoCardRepository.findLastRegister();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void guardar(proyectoCard dto) {
        try {
            proyectoCardRepository.save(dto);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void borrar(Integer id) {
        try {
            proyectoCardRepository.deleteById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean existsById(int id) {
        try {
            return proyectoCardRepository.existsById(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
