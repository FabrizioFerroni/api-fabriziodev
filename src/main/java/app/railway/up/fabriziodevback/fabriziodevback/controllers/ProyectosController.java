package app.railway.up.fabriziodevback.fabriziodevback.controllers;

import app.railway.up.fabriziodevback.fabriziodevback.dto.Mensaje;
import app.railway.up.fabriziodevback.fabriziodevback.dto.proyectoGeneralDto;
import app.railway.up.fabriziodevback.fabriziodevback.dto.proyectoCardDto;
import app.railway.up.fabriziodevback.fabriziodevback.entity.proyectoGeneral;
import app.railway.up.fabriziodevback.fabriziodevback.entity.proyectoCard;
import app.railway.up.fabriziodevback.fabriziodevback.security.entity.Usuario;
import app.railway.up.fabriziodevback.fabriziodevback.security.service.UsuarioService;
import app.railway.up.fabriziodevback.fabriziodevback.service.CloudinaryService;
import app.railway.up.fabriziodevback.fabriziodevback.service.proyectoGeneralService;
import app.railway.up.fabriziodevback.fabriziodevback.service.proyectoCardService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectosController {

    @Autowired
    CloudinaryService cService;

    @Autowired
    proyectoGeneralService serviceManager;

    @Autowired
    proyectoCardService  pcardService;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/general")
    @ResponseBody
    @ApiOperation(value = "Lista todos los registros")
    public ResponseEntity<List<proyectoGeneral>> listarTodos(){
        List<proyectoGeneral> prgen = serviceManager.listarTodos();
        return new ResponseEntity<>(prgen, HttpStatus.OK);
    }


    @GetMapping("/general/{id}")
    @ResponseBody
    @ApiOperation(value = "Listar por id el registro")
    public ResponseEntity<proyectoGeneral> getprgenbyid(@PathVariable Integer id) throws Exception{
        proyectoGeneral prgenbyid = serviceManager.getprgenById(id);
        if (prgenbyid == null) {
            throw new Exception("No se encontró el registro con id: " + id);
        }
        return new ResponseEntity<>(prgenbyid, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/general/agregar")
    @ApiOperation(value = "Inserta un nuevo registro a la BD")
    public ResponseEntity<proyectoGeneralDto> insertNewRegister(@Valid @RequestBody proyectoGeneralDto dto, BindingResult result, Authentication authentication) throws IOException, Exception{


        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Hubo un error al crear el nuevo registro"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getDescripcion() == null) {
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        proyectoGeneral prgen = new proyectoGeneral();

        prgen.setDescripcion(dto.getDescripcion());
        prgen.setCreatedAt(LocalDateTime.now());
        prgen.setUsuario_id(usuario.getId());

        serviceManager.guardar(prgen);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/general/{id}/editar")
    @ApiOperation(value = "Modifica un registro a la BD")
    public ResponseEntity<proyectoGeneralDto> updateRegister(@PathVariable  Integer id, @Valid @RequestBody proyectoGeneralDto dto, BindingResult result, Authentication authentication) throws IOException, Exception{
        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Hubo un error al crear el nuevo registro"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getDescripcion() == null) {
            return new ResponseEntity(new Mensaje("La descripcion es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

//        proyectoGeneral prgen = new proyectoGeneral();

        if(!serviceManager.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        proyectoGeneral prgen = serviceManager.getprgenById(id);

        prgen.setDescripcion(dto.getDescripcion());
        prgen.setCreatedAt(serviceManager.getprgenById(id).getCreatedAt());
        prgen.setEditedAt(LocalDateTime.now());
        prgen.setUsuario_id(usuario.getId());

        serviceManager.guardar(prgen);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/general/{id}/borrar")
    @ApiOperation(value = "Elimina un registro a la BD")
    public ResponseEntity<Integer> deleteRegister(@PathVariable("id") Integer id) throws IOException, Exception{
        if(!serviceManager.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        serviceManager.borrar(id);
        return new ResponseEntity(new Mensaje("Se borro el proyecto"), HttpStatus.OK);
    }


    @GetMapping("/card")
    public ResponseEntity<List<proyectoCard>> getproyectoscard(){
        List<proyectoCard> list = pcardService.listarTodos();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("card/{id}")
    public ResponseEntity<proyectoCard> getprcardbyid(@PathVariable Integer id) throws Exception {
        proyectoCard proy = pcardService.getprcardById(id);
        if (proy == null) {
            throw new Exception("No se encontró el registro con id: " + id);
        }
        return new ResponseEntity<>(proy, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("card/crear")
    public ResponseEntity<proyectoCardDto> addProy(@Valid proyectoCardDto dto, BindingResult result, @RequestParam MultipartFile file, Authentication authentication) throws IOException, Exception{
        BufferedImage bi = ImageIO.read(file.getInputStream());

        if(bi == null){
            return new ResponseEntity(new Mensaje("imagen no válida"), HttpStatus.BAD_REQUEST);
        }

        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Hubo un error al crear el nuevo registro"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getTitulo() == null) {
            return new ResponseEntity(new Mensaje("El titulo es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getDescripcion() == null) {
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        String folder = "proyectos";

        Map img_upload = cService.upload(folder, file);

        proyectoCard prcard = new proyectoCard();

        prcard.setTitulo(dto.getTitulo());
        prcard.setDescripcion(dto.getDescripcion());
        prcard.setPublic_id((String) img_upload.get("public_id"));
        prcard.setUrl_imagen((String) img_upload.get("url"));
        prcard.setEstado(dto.getEstado());
        prcard.setLink_demo(dto.getLink_demo());
        prcard.setLink_github(dto.getLink_github());
        prcard.setUsuario_id(usuario.getId());
        prcard.setCreatedAt(LocalDateTime.now());

        pcardService.guardar(prcard);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("card/{id}/editar")
    public ResponseEntity<proyectoCardDto> updProy(@PathVariable Integer id, @Valid proyectoCardDto dto, BindingResult result, @RequestParam MultipartFile file, Authentication authentication) throws IOException, Exception{
        BufferedImage bi = ImageIO.read(file.getInputStream());

        if(bi == null){
            return new ResponseEntity(new Mensaje("imagen no válida"), HttpStatus.BAD_REQUEST);
        }
        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Hubo un error al crear el nuevo registro"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getTitulo() == null) {
            return new ResponseEntity(new Mensaje("El titulo es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getDescripcion() == null) {
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        if (!pcardService.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        String folder = "proyectos";

        proyectoCard prcard = pcardService.getprcardById(id);

        String public_id = prcard.getPublic_id();

        if(public_id != null){
            cService.delete(public_id);
        }

        Map img_upload = cService.upload(folder, file);

        prcard.setTitulo(dto.getTitulo());
        prcard.setDescripcion(dto.getDescripcion());
        prcard.setPublic_id((String) img_upload.get("public_id"));
        prcard.setUrl_imagen((String) img_upload.get("url"));
        prcard.setEstado(dto.getEstado());
        prcard.setLink_demo(dto.getLink_demo());
        prcard.setLink_github(dto.getLink_github());
        prcard.setUsuario_id(usuario.getId());
        prcard.setCreatedAt(pcardService.getprcardById(id).getCreatedAt());
        prcard.setEditedAt(LocalDateTime.now());

        pcardService.guardar(prcard);

        return new ResponseEntity(new Mensaje("Se edito con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("card/{id}/borrar")
    public ResponseEntity<String> delProy(@PathVariable Integer id) throws IOException, Exception{
        if(!pcardService.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }
        proyectoCard prcard = pcardService.getprcardById(id);

        String public_id = prcard.getPublic_id();
        if(public_id != null) {
            cService.delete(public_id);
        }

        pcardService.borrar(id);


        return new ResponseEntity(new Mensaje("Se borro el proyecto"), HttpStatus.OK);

    }
}
