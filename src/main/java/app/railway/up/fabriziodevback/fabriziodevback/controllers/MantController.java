package app.railway.up.fabriziodevback.fabriziodevback.controllers;

import app.railway.up.fabriziodevback.fabriziodevback.dto.Mensaje;
import app.railway.up.fabriziodevback.fabriziodevback.dto.maintenanceDto;
import app.railway.up.fabriziodevback.fabriziodevback.entity.maintenance;
import app.railway.up.fabriziodevback.fabriziodevback.security.entity.Usuario;
import app.railway.up.fabriziodevback.fabriziodevback.security.service.UsuarioService;
import app.railway.up.fabriziodevback.fabriziodevback.service.maintenanceService;
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
@RequestMapping("/api/")
public class MantController {

    @Autowired
    maintenanceService service;

    @Autowired
    UsuarioService usuarioService;
    @GetMapping("maintenance")
    public ResponseEntity<List<maintenance>> getData(){
        List<maintenance> list = service.listarTodos();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("maintenance")
    public ResponseEntity<maintenanceDto> postData(Authentication authentication) throws IOException, Exception{

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        maintenance mant = new maintenance();

        mant.setEstado(false);
        mant.setEditedAt(LocalDateTime.now());
        mant.setUsuario_id(usuario.getId());

        service.guardar(mant);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("maintenance")
    public ResponseEntity<maintenanceDto> editData(@RequestBody @Valid maintenanceDto dto, BindingResult result, Authentication authentication) throws IOException, Exception{

        if (result.hasErrors()) {
            return new ResponseEntity(new Mensaje("Hubo un error al crear el nuevo registro"), HttpStatus.BAD_REQUEST);
        }

        if (dto.getEstado() == null) {
            return new ResponseEntity(new Mensaje("Debes pasar un estado"), HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        maintenance mant = service.getById(1);

        mant.setEstado(dto.getEstado());
        mant.setEditedAt(LocalDateTime.now());
        mant.setUsuario_id(usuario.getId());

        service.guardar(mant);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);

    }
}
