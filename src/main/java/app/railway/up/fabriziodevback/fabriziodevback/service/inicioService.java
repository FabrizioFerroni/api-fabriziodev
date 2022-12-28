package app.railway.up.fabriziodevback.fabriziodevback.service;

import app.railway.up.fabriziodevback.fabriziodevback.entity.inicio;
import app.railway.up.fabriziodevback.fabriziodevback.repository.inicioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class inicioService {
    private static final Logger logger = LoggerFactory.getLogger(inicioService.class);

    private final Path rootFolder = Paths.get("uploads/docs");
    @Autowired
    inicioRepository inicioRepo;

    public List<inicio> listarTodos() {
        try{
            return inicioRepo.findAllByOrderByIdDesc();
        }
        catch(Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    public inicio getById(Integer id) throws Exception {
        try{
            inicio ini = inicioRepo.findById(id).orElseThrow(() -> new Exception("No se encontró el registro con id: " + id));
            return ini;
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    public List<inicio> getLastreg() throws Exception {
        try{return inicioRepo.findLastRegister();}
        catch(Exception e){logger.error(e.getMessage());}
        return null;
    }

    public void guardar(inicio dto) {
        try{
            inicioRepo.save(dto);
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public void borrar(Integer id) {
        try {
            inicioRepo.deleteById(id);
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public boolean existsById(int id) {
        try {
            return inicioRepo.existsById(id);
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

    public Resource load(String name) throws Exception {
        try{
            Path file = rootFolder.resolve(name);
            Resource resource = new UrlResource(file.toUri());
            return resource;
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
}
