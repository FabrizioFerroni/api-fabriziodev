package app.railway.up.fabriziodevback.fabriziodevback.controllers;

import app.railway.up.fabriziodevback.fabriziodevback.dto.Mensaje;
import app.railway.up.fabriziodevback.fabriziodevback.dto.quiensoyCardDto;
import app.railway.up.fabriziodevback.fabriziodevback.dto.quiensoyGeneralDto;
import app.railway.up.fabriziodevback.fabriziodevback.dto.quiensoyServiceDto;
import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyCard;
import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyGeneral;
import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyService;
import app.railway.up.fabriziodevback.fabriziodevback.security.entity.Usuario;
import app.railway.up.fabriziodevback.fabriziodevback.security.service.UsuarioService;
import app.railway.up.fabriziodevback.fabriziodevback.service.CloudinaryService;
import app.railway.up.fabriziodevback.fabriziodevback.service.quiensoyCardService;
import app.railway.up.fabriziodevback.fabriziodevback.service.quiensoyGeneralService;
import app.railway.up.fabriziodevback.fabriziodevback.service.quiensoyServiceService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quien-soy")
public class QuiensoyController {
    @Autowired
    CloudinaryService cService;

    @Autowired
    quiensoyGeneralService qGeneral;

    @Autowired
    quiensoyCardService qCard;

    @Autowired
    quiensoyServiceService qService;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/general")
    public ResponseEntity<List<quiensoyGeneral>> getdata(){
        List<quiensoyGeneral> list = qGeneral.listarTodos();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("general/{id}")
    public ResponseEntity<quiensoyGeneral> getdatabyid(@PathVariable Integer id) throws Exception {
        quiensoyGeneral qsogen = qGeneral.getById(id);
        if (qsogen == null) {
            throw new Exception("No se encontró el registro con id: " + id);
        }
        return new ResponseEntity<>(qsogen, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("general/crear")
    public ResponseEntity<quiensoyGeneralDto> addregqs(@Valid @RequestBody quiensoyGeneralDto dto, BindingResult result, Authentication authentication) throws IOException, Exception{

        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Hubo un error al crear el nuevo registro"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getDescripcion() == null) {
            return new ResponseEntity(new Mensaje("La descripcion es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        if(dto.getYear() == null){
            return new ResponseEntity(new Mensaje("La cantidad de años es obligatoria"), HttpStatus.BAD_REQUEST);
        }

//        Falta comprobar si el dato ingresado no es tipo numero en year

        if (dto.getDescYear() == null) {
            return new ResponseEntity(new Mensaje("La descripción del año es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        quiensoyGeneral qsgen = new quiensoyGeneral();

        qsgen.setDescYear(dto.getDescYear());
        qsgen.setDescripcion(dto.getDescripcion());
        qsgen.setYear(dto.getYear());
        qsgen.setUsuario_id(usuario.getId());
        qsgen.setCreatedAt(LocalDateTime.now());

        qGeneral.guardar(qsgen);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("general/{id}/editar")
    public ResponseEntity<quiensoyGeneralDto> updquiensoy(@PathVariable Integer id, @Valid @RequestBody quiensoyGeneralDto dto, BindingResult result, Authentication authentication) throws IOException, Exception{

        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Hubo un error al crear el nuevo registro"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getDescripcion() == null) {
            return new ResponseEntity(new Mensaje("La descripcion es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        if(dto.getYear() == null){
            return new ResponseEntity(new Mensaje("La cantidad de años es obligatoria"), HttpStatus.BAD_REQUEST);
        }

//        Falta comprobar si el dato ingresado no es tipo numero en year

        if (dto.getDescYear() == null) {
            return new ResponseEntity(new Mensaje("La descripción del año es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        if (!qGeneral.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();



        quiensoyGeneral qsgen = qGeneral.getById(id);



        qsgen.setDescYear(dto.getDescYear());
        qsgen.setDescripcion(dto.getDescripcion());
        qsgen.setYear(dto.getYear());
        qsgen.setUsuario_id(usuario.getId());
        qsgen.setCreatedAt(qGeneral.getById(id).getCreatedAt());
        qsgen.setEditedAt(LocalDateTime.now());

        qGeneral.guardar(qsgen);

        return new ResponseEntity(new Mensaje("Se edito con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("general/{id}/borrar")
    public ResponseEntity<String> delQuiensoy(@PathVariable Integer id) throws IOException, Exception{
        if(!qGeneral.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        qGeneral.borrar(id);

        return new ResponseEntity(new Mensaje("Se borro con éxito"), HttpStatus.OK);

    }


    @GetMapping("/card")
    public ResponseEntity<List<quiensoyCard>> getdataCard(){
        List<quiensoyCard> list = qCard.listarTodos();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("card/{id}")
    public ResponseEntity<quiensoyCard> getdatabyidCard(@PathVariable Integer id) throws Exception {
        quiensoyCard qsocard = qCard.getById(id);
        if (qsocard == null) {
            throw new Exception("No se encontró el registro con id: " + id);
        }
        return new ResponseEntity<>(qsocard, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("card/crear")
    public ResponseEntity<quiensoyGeneralDto> addregqs(@RequestParam MultipartFile file, @Valid quiensoyCardDto dto, BindingResult result, Authentication authentication) throws IOException, Exception{

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

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        String folder = "quien-soy";

        Map img_upload = cService.upload(folder, file);

        quiensoyCard qscard = new quiensoyCard();

        qscard.setTitulo(dto.getTitulo());
        qscard.setPublic_id((String) img_upload.get("public_id"));
        qscard.setUrl_imagen((String) img_upload.get("url"));
        qscard.setUsuario_id(usuario.getId());
        qscard.setCreatedAt(LocalDateTime.now());

        qCard.guardar(qscard);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("card/{id}/editar")
    public ResponseEntity<quiensoyGeneralDto> updquiensoyCard(@RequestParam MultipartFile file, @PathVariable Integer id, @Valid quiensoyCardDto dto, BindingResult result, Authentication authentication) throws IOException, Exception{
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


        if (!qCard.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();


        String folder = "quien-soy";




        quiensoyCard qscard = qCard.getById(id);
        String public_id = qscard.getPublic_id();

        if(public_id != null){
            cService.delete(public_id);
        }

        Map img_upload = cService.upload(folder, file);
        qscard.setTitulo(dto.getTitulo());
        qscard.setPublic_id((String) img_upload.get("public_id"));
        qscard.setUrl_imagen((String) img_upload.get("url"));
        qscard.setCreatedAt(qscard.getCreatedAt());
        qscard.setEditedAt(LocalDateTime.now());
        qscard.setUsuario_id(usuario.getId());

        qCard.guardar(qscard);

        return new ResponseEntity(new Mensaje("Se edito con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("card/{id}/borrar")
    public ResponseEntity<String> delQuiensoyCard(@PathVariable Integer id) throws IOException, Exception{
        if(!qCard.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        quiensoyCard qscard = qCard.getById(id);
        String public_id = qscard.getPublic_id();

        if(public_id != null){
            cService.delete(public_id);
        }

        qCard.borrar(id);

        return new ResponseEntity(new Mensaje("Se borro con éxito"), HttpStatus.OK);

    }

    @GetMapping("/service")
    public ResponseEntity<List<quiensoyService>> getdataService(){
        List<quiensoyService> list = qService.listarTodos();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("service/{id}")
    public ResponseEntity<quiensoyService> getdatabyidServ(@PathVariable Integer id) throws Exception {
        quiensoyService qsoserv = qService.getById(id);
        if (qsoserv == null) {
            throw new Exception("No se encontró el registro con id: " + id);
        }
        return new ResponseEntity<>(qsoserv, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("service/crear")
    public ResponseEntity<quiensoyServiceDto> addregqserv(@RequestParam MultipartFile file, @Valid quiensoyServiceDto dto, BindingResult result, Authentication authentication) throws IOException, Exception{

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
            return new ResponseEntity(new Mensaje("La descripcion es obligatoria"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        String folder = "quien-soy";

        Map img_upload = cService.upload(folder, file);

        quiensoyService qsserv = new quiensoyService();

        qsserv.setTitulo(dto.getTitulo());
        qsserv.setDescripcion(dto.getDescripcion());
        qsserv.setPublic_id((String) img_upload.get("public_id"));
        qsserv.setUrl_imagen((String) img_upload.get("url"));
        qsserv.setUsuario_id(usuario.getId());
        qsserv.setCreatedAt(LocalDateTime.now());

        qService.guardar(qsserv);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("service/{id}/editar")
    public ResponseEntity<quiensoyServiceDto> updquiensoyServ(@RequestParam MultipartFile file, @PathVariable Integer id, @Valid quiensoyServiceDto dto, BindingResult result, Authentication authentication) throws IOException, Exception{
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
            return new ResponseEntity(new Mensaje("La descripcion es obligatoria"), HttpStatus.BAD_REQUEST);
        }


        if (!qService.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();


        String folder = "quien-soy";




        quiensoyService qsserv = qService.getById(id);
        String public_id = qsserv.getPublic_id();

        if(public_id != null){
            cService.delete(public_id);
        }

        Map img_upload = cService.upload(folder, file);
        qsserv.setTitulo(dto.getTitulo());
        qsserv.setDescripcion(dto.getDescripcion());
        qsserv.setPublic_id((String) img_upload.get("public_id"));
        qsserv.setUrl_imagen((String) img_upload.get("url"));
        qsserv.setCreatedAt(qsserv.getCreatedAt());
        qsserv.setEditedAt(LocalDateTime.now());
        qsserv.setUsuario_id(usuario.getId());

        qService.guardar(qsserv);

        return new ResponseEntity(new Mensaje("Se edito con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("service/{id}/borrar")
    public ResponseEntity<String> delQuiensoyServ(@PathVariable Integer id) throws IOException, Exception{
        if(!qService.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        quiensoyService qsserv = qService.getById(id);
        String public_id = qsserv.getPublic_id();

        if(public_id != null){
            cService.delete(public_id);
        }

        qService.borrar(id);

        return new ResponseEntity(new Mensaje("Se borro con éxito"), HttpStatus.OK);

    }
}
