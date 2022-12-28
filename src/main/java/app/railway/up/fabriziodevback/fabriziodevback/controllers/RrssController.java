package app.railway.up.fabriziodevback.fabriziodevback.controllers;

import app.railway.up.fabriziodevback.fabriziodevback.dto.Mensaje;
import app.railway.up.fabriziodevback.fabriziodevback.dto.quiensoyGeneralDto;
import app.railway.up.fabriziodevback.fabriziodevback.dto.rrssDto;
import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyGeneral;
import app.railway.up.fabriziodevback.fabriziodevback.entity.rrss;
import app.railway.up.fabriziodevback.fabriziodevback.security.entity.Usuario;
import app.railway.up.fabriziodevback.fabriziodevback.security.service.UsuarioService;
import app.railway.up.fabriziodevback.fabriziodevback.service.rrssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rrss")
public class RrssController {
    @Autowired
    rrssService rService;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping()
    public ResponseEntity<List<rrss>> getdata(){
        List<rrss> list = rService.listarTodos();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<rrss> getdatabyid(@PathVariable Integer id) throws Exception {
        rrss misredes = rService.getById(id);
        return new ResponseEntity<>(misredes, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<rrssDto> addregqs(@Valid @RequestBody rrssDto dto, BindingResult result, Authentication authentication) throws IOException, Exception {

        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Hubo un error al crear el nuevo registro"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getNombre() == null) {
            return new ResponseEntity(new Mensaje("El nombre de la red social es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getIcon() == null) {
            return new ResponseEntity(new Mensaje("El icono de la red social es obligatorio"), HttpStatus.BAD_REQUEST);
        }


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        rrss misredes = new rrss();

        misredes.setNombre(dto.getNombre());
        misredes.setIcon(dto.getIcon());
        misredes.setCreatedAt(LocalDateTime.now());
        misredes.setUsuario_id(usuario.getId());

        rService.guardar(misredes);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/editar")
    public ResponseEntity<rrssDto> updregqs(@PathVariable Integer id, @Valid @RequestBody rrssDto dto, BindingResult result, Authentication authentication) throws IOException, Exception {

        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Hubo un error al crear el nuevo registro"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getNombre() == null) {
            return new ResponseEntity(new Mensaje("El nombre de la red social es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getIcon() == null) {
            return new ResponseEntity(new Mensaje("El icono de la red social es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (!rService.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        rrss misredes = rService.getById(id);

        misredes.setNombre(dto.getNombre());
        misredes.setIcon(dto.getIcon());
        misredes.setCreatedAt(misredes.getCreatedAt());
        misredes.setEditedAt(LocalDateTime.now());
        misredes.setUsuario_id(usuario.getId());

        rService.guardar(misredes);

        return new ResponseEntity(new Mensaje("Se edito con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}/borrar")
    public ResponseEntity<String> delreg (@PathVariable Integer id){
        if (!rService.existsById(id)){
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        rService.borrar(id);

        return new ResponseEntity(new Mensaje("Se borro con éxito"), HttpStatus.OK);
    }
}
