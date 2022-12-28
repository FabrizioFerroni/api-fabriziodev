package app.railway.up.fabriziodevback.fabriziodevback.controllers;

import app.railway.up.fabriziodevback.fabriziodevback.FabriziodevBackApplication;
import app.railway.up.fabriziodevback.fabriziodevback.dto.Mensaje;
import app.railway.up.fabriziodevback.fabriziodevback.dto.contactoDto;
import app.railway.up.fabriziodevback.fabriziodevback.entity.contacto;
import app.railway.up.fabriziodevback.fabriziodevback.service.contactoService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ContactoController {

    private static final Logger logger = LoggerFactory.getLogger(ContactoController.class);

    @Autowired
    contactoService contactoService;

    @Value("${spring.mail.username}")
    private String mailFrom;


    @GetMapping("contactos")
    @ResponseBody
    @ApiIgnore
    public ResponseEntity<List<contacto>> listarTodos() {
        List lista = contactoService.listarContactos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("contacto/{id}")
    @ResponseBody
    @ApiIgnore
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<contacto> listarPorId(@PathVariable("id") Integer id) throws Exception {
        contacto contact = contactoService.listarPorId(id);
        if (!contactoService.existsById(id)) {
            return new ResponseEntity(new Mensaje("No existe ningún contacto con ese id"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(contact);
    }

    @PostMapping("contactame")
    @ApiOperation(value = "Metodo para contactarse conmigo, guarda un registro en la bd mientras que me envia un correo electronico")
    public ResponseEntity<contactoDto> sendEmailTemplateContacto(@Valid @RequestBody contactoDto dto) throws MessagingException {
        try {
            contacto contacto = new contacto();
            contacto.setNombre(dto.getNombre());
            dto.setNombre(dto.getNombre());
            contacto.setEmail(dto.getEmail());
            dto.setEmail(dto.getEmail());
            contacto.setMensaje(dto.getMensaje());
            dto.setMensaje(dto.getMensaje());
            contacto.setCreatedAt(LocalDateTime.now());
            if (dto.getTelefono() != null || dto.getTelefono() != "") {
                contacto.setTelefono(dto.getTelefono());
                dto.setTelefono(dto.getTelefono());
            } else {
                contacto.setTelefono(null);
                dto.setTelefono(null);
            }
            if (dto.getSubject() != null) {
                contacto.setSubject(dto.getSubject());
                dto.setSubject(dto.getSubject());
            } else {
                contacto.setSubject("Te han contactado desde tu página web");
                dto.setSubject("Te han contactado desde tu página web");
            }
            contacto.setMailCc(dto.getEmail());
            dto.setMailCc(dto.getEmail());
            dto.setMailFrom(dto.getEmail());
            contacto.setMailFrom(dto.getEmail());


            contactoService.sendEmailContacto(dto);
            ;
            contactoService.save(contacto);

            return new ResponseEntity(new Mensaje("Gracias por contactarte conmigo, pronto te responderemos tu mensaje"), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        }
        return null;

    }
}
