package app.railway.up.fabriziodevback.fabriziodevback.service;
import app.railway.up.fabriziodevback.fabriziodevback.controllers.ContactoController;
import app.railway.up.fabriziodevback.fabriziodevback.dto.contactoDto;
import app.railway.up.fabriziodevback.fabriziodevback.entity.contacto;
import  app.railway.up.fabriziodevback.fabriziodevback.repository.contactoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class contactoService {

    private static final Logger logger = LoggerFactory.getLogger(contactoService.class);

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${mailContact}")
    private String mailTo;

    @Value("${spring.mail.host}")
    private String hostEmail;

    @Value("${spring.mail.username}")
    private String userEmail;

    @Value("${spring.mail.password}")
    private String passEmail;

    @Value("${spring.mail.name}")
    private String name;

    @Value("${hostfront.dns}")
    private String urlFront;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Autowired
    contactoRepository contactoRepository;


    /**
     * correo electrónico del remitente
     */
    private String from;

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    //Listar todos los contactos
    public List<contacto> listarContactos() {
        try {
            return contactoRepository.findAllByOrderByIdDesc();
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    //Listar contacto por id
    public contacto listarPorId(Integer id) throws Exception {
        try {
            contacto cont = contactoRepository.findById(id).orElseThrow(() -> new Exception("No se envio nigun contacto con este id"));
            return cont;
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    public void save(contacto dto) {
        try {
            contactoRepository.save(dto);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void borrar(Integer id) {
        try{
            contactoRepository.deleteById(id);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public boolean existsById(int id) {
        try {
            return contactoRepository.existsById(id);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public void sendEmailContacto(contactoDto dto) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("nombre", dto.getNombre());
            model.put("email", dto.getEmail());
            model.put("asunto", dto.getSubject());
            model.put("mensaje", dto.getMensaje());
            if (dto.getTelefono() != null || dto.getTelefono() != "") {
                model.put("telefono", dto.getTelefono());
            }
            if (dto.getTelefono() == "" || dto.getTelefono() == null) {
                dto.setTelefono(null);
            }
            String dayhoy = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
            String horahoy = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
            String nameRem = dto.getNombre();
            String mailFrom2 = dto.getEmail();
            model.put("fecha", dayhoy);
            model.put("hora", horahoy);
            model.put("url", urlFront);
            context.setVariables(model);
            String htmlText = templateEngine.process("email-contact", context);
            InternetAddress ia=new InternetAddress(mailFrom2,nameRem);
            helper.setFrom(ia);
            helper.setCc(dto.getMailCc());
            helper.setTo(mailTo);
            helper.setSubject(dto.getSubject());
            helper.setText(htmlText, true);

            javaMailSender.send(message);
            sendEmailResp(dto);
        }
        catch (MessagingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());

        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendEmailResp(contactoDto dto) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("nombre", dto.getNombre());
            model.put("email", dto.getEmail());
            model.put("asunto", dto.getSubject());
            model.put("mensaje", dto.getMensaje());
            if (dto.getTelefono() != null || dto.getTelefono() != "") {
                model.put("telefono", dto.getTelefono());
            }
            if (dto.getTelefono() == "" || dto.getTelefono() == null) {
                dto.setTelefono(null);
            }
            context.setVariables(model);
            String htmlText = templateEngine.process("email-aviso", context);
            helper.setFrom(new InternetAddress(mailFrom, name));
            helper.setTo(dto.getEmail());
            helper.setSubject("Gracias por contactarte conmigo");
            helper.setText(htmlText, true);

            javaMailSender.send(message);
        }
        catch (MessagingException e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
