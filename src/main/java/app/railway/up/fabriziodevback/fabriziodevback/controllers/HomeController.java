package app.railway.up.fabriziodevback.fabriziodevback.controllers;

import app.railway.up.fabriziodevback.fabriziodevback.dto.Mensaje;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@RestController
@ApiIgnore
public class HomeController {
    @GetMapping("/")
    public ModelAndView homeredirect(HttpServletResponse response) throws IOException {
        try{
            response.sendRedirect("/swagger-ui/index.html");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/api/home")
    @ResponseBody
    public ResponseEntity<?> home() {
        return ResponseEntity.ok(new Mensaje("Bienvenido a la API de FabrizioDev"));
    }

    @GetMapping("/api")
    public ModelAndView apiredirect(HttpServletResponse response) throws IOException {
        try{
            response.sendRedirect("/swagger-ui/index.html");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/logs")
    public String viewLog() throws IOException{
        String nombreArchivo = "logs\\spring.log";

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(nombreArchivo);
            bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder("");
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                // Lee línea por línea, omitiendo los saltos de línea
                stringBuilder.append(linea + "\n");
            }

//            System.out.println("Contenido: " + stringBuilder.toString());
            return stringBuilder.toString();

        } catch (IOException e) {
            System.out.println("Excepción leyendo archivo: " + e.getMessage());
        } finally {
            try {
                if (fileReader != null)
                    fileReader.close();
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                System.out.println("Excepción cerrando: " + e.getMessage());
            }
        }
        return null;
    }
}
