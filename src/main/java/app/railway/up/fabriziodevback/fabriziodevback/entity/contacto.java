package app.railway.up.fabriziodevback.fabriziodevback.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @NotNull
    private String nombre;

    @Column(nullable = false)
    @NotNull
    private String email;

    private String mailFrom;
    private String mailCc;

    private String telefono;

    private String subject;

    @Column(nullable = false)
    @NotNull
    private String mensaje;


    private LocalDateTime createdAt;

    public contacto() {
    }

    public contacto(String nombre, String email, String mailFrom, String mailCc, String telefono, String subject, String mensaje, LocalDateTime createdAt) {
        this.nombre = nombre;
        this.email = email;
        this.mailFrom = mailFrom;
        this.mailCc = mailCc;
        this.telefono = telefono;
        this.subject = subject;
        this.mensaje = mensaje;
        this.createdAt = createdAt;
    }
}
