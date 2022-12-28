package app.railway.up.fabriziodevback.fabriziodevback.controllers;

import app.railway.up.fabriziodevback.fabriziodevback.dto.Mensaje;
import app.railway.up.fabriziodevback.fabriziodevback.dto.inicioDto;
import app.railway.up.fabriziodevback.fabriziodevback.dto.quiensoyServiceDto;
import app.railway.up.fabriziodevback.fabriziodevback.entity.inicio;
import app.railway.up.fabriziodevback.fabriziodevback.entity.quiensoyService;
import app.railway.up.fabriziodevback.fabriziodevback.security.entity.Usuario;
import app.railway.up.fabriziodevback.fabriziodevback.security.service.UsuarioService;
import app.railway.up.fabriziodevback.fabriziodevback.service.CloudinaryService;
import app.railway.up.fabriziodevback.fabriziodevback.service.inicioService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/inicio")
public class InicioController {

    @Value("${web.upload-path-docs}")
    private String url;

    @Value("${web.upload-path-uploads}")
    private String uploads;

    @Value("${host.dns}")
    private String host;

    @Autowired
    CloudinaryService cService;

    @Autowired
    inicioService iniServ;

    @Autowired
    UsuarioService usuarioService;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ROOT);
    }

    @GetMapping()
    public ResponseEntity<List<inicio>> getdataInicio() {
        List<inicio> list = iniServ.listarTodos();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<inicio> getdatabyidIni(@PathVariable Integer id) throws Exception {
        inicio ini = iniServ.getById(id);
        return new ResponseEntity<>(ini, HttpStatus.OK);
    }

    //Metodo para descargar CV automaticamente.
    @GetMapping("download_cv/{cvNAME}")
    @ApiIgnore
    public ResponseEntity<Resource> getFile(@PathVariable String cvNAME) throws Exception {
        Resource resource = iniServ.load(cvNAME);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @ApiOperation(value = "Obtiene el cv según su nombre")
    @ApiIgnore
    @GetMapping(value = "cv/{cvNAME}")
    public ResponseEntity<byte[]> getCV(@PathVariable("cvNAME") String cvNAME) throws Exception {
        if (cvNAME == null) {
            return new ResponseEntity(new Mensaje("Por favor seleccione un CV valido"), HttpStatus.NO_CONTENT);
        }

        try {
            String fileName = url + "/" + cvNAME;
            Path path = Paths.get(fileName);
            File f = path.toFile();
            if (!f.exists()) {
                String fileDefault = uploads + "/" + "default.jpg";
                Path pathDefault = Paths.get(fileDefault);
                byte[] imageDefault = Files.readAllBytes(pathDefault);
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageDefault);
            }

            byte[] image = Files.readAllBytes(path);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(image);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResponseEntity(new Mensaje("Error al mostrar EL PDF"), HttpStatus.CONFLICT);
        }

    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<inicioDto> addregqserv(@RequestParam MultipartFile file, @RequestParam MultipartFile cv, @Valid inicioDto dto, BindingResult result, Authentication authentication) throws IOException, Exception {

        BufferedImage bi = ImageIO.read(file.getInputStream());

        if (bi == null) {
            return new ResponseEntity(new Mensaje("imagen no válida"), HttpStatus.BAD_REQUEST);
        }

        if (cv.isEmpty()) {
            return new ResponseEntity(new Mensaje("No se ha seleccionado ningún Cv"), HttpStatus.BAD_REQUEST);
        }
        if (cv.getSize() > 5000000) {
            return new ResponseEntity(new Mensaje("El archivo es demasiado grande"), HttpStatus.BAD_REQUEST);
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

        inicio ini = new inicio();

        Path rootFolder = Paths.get(url);
        if (!Files.exists(rootFolder)) {
            Files.createDirectories(rootFolder);
        }

        if (!cv.isEmpty()) {
            String ext = FilenameUtils.getExtension(cv.getOriginalFilename());
            if (ext.equals("pdf") || ext.equals("docx") || ext.equals("doc")) {
                String rutaAbsoluta = rootFolder.toFile().getAbsolutePath();
                try {
                    byte[] bytesCv = cv.getBytes();
                    Random random = new Random();
                    int r = random.nextInt(999);
                    String nFn = r + "_" + toSlug(dto.getTitulo()) + "." + ext;
                    Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nFn);
                    Files.write(rutaCompleta, bytesCv);
                    ini.setCv_name(nFn);
                    String urlIMG = host + "/api/inicio/download_cv/" + nFn;
                    ini.setCv_url(urlIMG);
                    ini.setCv_ruta(rootFolder + "\\" + nFn);

                } catch (IOException e) {
                    return new ResponseEntity(new Mensaje("Se ha producido un error: " + e), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(new Mensaje("Archivos no soportados por el servidor. Los archivos deberan ser del formato: PDF, DOCX, DOC. \nEstas mandando un archivo de esta extension: ." + ext), HttpStatus.BAD_REQUEST);

            }

        }

        String folder = "inicio";

        Map img_upload = cService.upload(folder, file);


        ini.setTitulo(dto.getTitulo());
        ini.setDescripcion(dto.getDescripcion());
        ini.setPublic_id((String) img_upload.get("public_id"));
        ini.setUrl_imagen((String) img_upload.get("url"));
        ini.setUsuario_id(usuario.getId());
        ini.setCreatedAt(LocalDateTime.now());
        ini.setCv_desc("cv-fabrizio-ferroni.pdf");

        iniServ.guardar(ini);

        return new ResponseEntity(new Mensaje("Se guardo con éxito el registro"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/editar")
    public ResponseEntity<quiensoyServiceDto> updquiensoyini(@RequestParam MultipartFile file, @RequestParam MultipartFile cv, @PathVariable Integer id, @Valid inicioDto dto, BindingResult result, Authentication authentication) throws IOException, Exception {
        BufferedImage bi = ImageIO.read(file.getInputStream());

        if (bi == null) {
            return new ResponseEntity(new Mensaje("imagen no válida"), HttpStatus.BAD_REQUEST);
        }

        if (cv.isEmpty()) {
            return new ResponseEntity(new Mensaje("No se ha seleccionado ningún Cv"), HttpStatus.BAD_REQUEST);
        }
        if (cv.getSize() > 5000000) {
            return new ResponseEntity(new Mensaje("El archivo es demasiado grande"), HttpStatus.BAD_REQUEST);
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

        if (!iniServ.existsById(id)) {
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

        inicio ini = iniServ.getById(id);

        String folder = "inicio";


        String cvNAME = ini.getCv_name();

        Path rootFolder = Paths.get(url);
        if (cvNAME == null) {
            if (!cv.isEmpty()) {
                String ext = FilenameUtils.getExtension(cv.getOriginalFilename());
                if (ext.equals("pdf") || ext.equals("docx") || ext.equals("doc")) {
                    String rutaAbsoluta = rootFolder.toFile().getAbsolutePath();
                    try {
                        byte[] bytesCv = cv.getBytes();
                        Random random = new Random();
                        int r = random.nextInt(999);
                        String nFn = r + "_CV-Fabrizio-Ferroni." + ext;
                        Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nFn);
                        Files.write(rutaCompleta, bytesCv);
                        ini.setCv_name(nFn);
                        String urlIMG = host + "/api/inicio/download_cv/" + nFn;
                        ini.setCv_url(urlIMG);
                        ini.setCv_ruta(rootFolder + "\\" + nFn);

                    } catch (IOException e) {
                        return new ResponseEntity(new Mensaje("Se ha producido un error: " + e), HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity(new Mensaje("Archivos no soportados por el servidor. Los archivos deberan ser del formato: PDF, DOCX, DOC. \nEstas mandando un archivo de esta extension: ." + ext), HttpStatus.BAD_REQUEST);
                }
            }

            /*cv.setNombreCv(cvDto.getNombreCv());
            cv.setDescripcionCv(cvDto.getDescripcionCv());
            String nomDesc = toSlug(cvDto.getNombreCv()) + ".pdf";
            cv.setNombredown(nomDesc);
            cv.setCreatedAt(cvService.listarPorId(id).getCreatedAt());
            cv.setEditedAt(LocalDateTime.now());
            cvService.guardar(cv);
            return new ResponseEntity(new Mensaje("Se edito correctamente el cv"), HttpStatus.OK);*/
        } else {
            Path rutaFile = Paths.get(ini.getCv_ruta());

            // Busca si existe un archivo en la ruta y si no hay elimina la carpeta.
            if (Files.exists(rutaFile)) {
                if (!cv.isEmpty()) {
                    Files.deleteIfExists(rutaFile);
                }
            }
            //Le manda la imagen, el producto y la carpeta root al metodo de subirImagen para que guarde la imagen
            if (!cv.isEmpty()) {
                String ext = FilenameUtils.getExtension(cv.getOriginalFilename());
                if (ext.equals("pdf") || ext.equals("docx") || ext.equals("doc")) {
                    String rutaAbsoluta = rootFolder.toFile().getAbsolutePath();
                    try {
                        byte[] bytesCv = cv.getBytes();
                        Random random = new Random();
                        int r = random.nextInt(999);
                        String nFn = r + "_CV-Fabrizio-Ferroni." + ext;
                        Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nFn);
                        Files.write(rutaCompleta, bytesCv);
                        ini.setCv_name(nFn);
                        String urlIMG = host + "/api/inicio/download_cv/" + nFn;
                        ini.setCv_url(urlIMG);
                        ini.setCv_ruta(rootFolder + "\\" + nFn);

                    } catch (Exception e) {
                        return new ResponseEntity(new Mensaje("Se ha producido un error: " + e), HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity(new Mensaje("Archivos no soportados por el servidor. Los archivos deberan ser del formato: PDF, DOCX, DOC. \nEstas mandando un archivo de esta extension: ." + ext), HttpStatus.BAD_REQUEST);
                }
            }

        }
        String public_id = ini.getPublic_id();

        if (public_id != null) {
            cService.delete(public_id);
        }

        Map img_upload = cService.upload(folder, file);
        ini.setTitulo(dto.getTitulo());
        ini.setDescripcion(dto.getDescripcion());
        ini.setPublic_id((String) img_upload.get("public_id"));
        ini.setUrl_imagen((String) img_upload.get("url"));
        ini.setCreatedAt(ini.getCreatedAt());
        ini.setEditedAt(LocalDateTime.now());
        ini.setUsuario_id(usuario.getId());
        ini.setCv_desc("cv-fabrizio-ferroni.pdf");

        iniServ.guardar(ini);

        return new ResponseEntity(new Mensaje("Se edito con éxito el registro"), HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/borrar")
    public ResponseEntity<String> delQuiensoyini(@PathVariable Integer id) throws IOException, Exception {
        if (!iniServ.existsById(id)) {
            return new ResponseEntity(new Mensaje("El id ingresado no es valido"), HttpStatus.BAD_REQUEST);
        }

        inicio ini = iniServ.getById(id);
        String public_id = ini.getPublic_id();
        String cvNAME = ini.getCv_name();

        if (cvNAME != null) {
            Path rutaFile = Paths.get(ini.getCv_ruta());
            if (Files.exists(rutaFile)) {
                Files.deleteIfExists(rutaFile);
                if (!Files.list(Paths.get(url)).findAny().isPresent()) {
                    Files.delete(Paths.get(url));
                }
            }
        }

        if (public_id != null) {
            cService.delete(public_id);
        }

        iniServ.borrar(id);

        return new ResponseEntity(new Mensaje("Se borro con éxito"), HttpStatus.OK);

    }


}


