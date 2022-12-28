package app.railway.up.fabriziodevback.fabriziodevback.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class inicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @NotNull
    @Column(name = "descripcion", nullable = false, length = 1024)
    private String descripcion;


    private String public_id;

    private String url_imagen;

    @Column(nullable = false)
    @NotNull
    private Integer usuario_id;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    //    parte cv
    private String cv_name;
    private String cv_url;
    private String cv_desc;
    private String cv_ruta;

    public inicio() {
    }

    public inicio(int id, String titulo, String descripcion, String public_id, String url_imagen, Integer usuario_id, LocalDateTime createdAt, LocalDateTime editedAt, String cv_name, String cv_url, String cv_desc, String cv_ruta) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.public_id = public_id;
        this.url_imagen = url_imagen;
        this.usuario_id = usuario_id;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.cv_name = cv_name;
        this.cv_url = cv_url;
        this.cv_desc = cv_desc;
        this.cv_ruta = cv_ruta;
    }

}
