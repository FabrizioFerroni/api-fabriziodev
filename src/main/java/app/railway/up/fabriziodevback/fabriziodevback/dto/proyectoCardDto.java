package app.railway.up.fabriziodevback.fabriziodevback.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class proyectoCardDto {
    @NotNull
    @Column(name="titulo", nullable=false, length=255)
    private String titulo;

    @NotNull
    @Column(name="descripcion", nullable=false, length=1024)
    private String descripcion;

    private String public_id;

    private String url_imagen;

    private Boolean estado;

    private String link_demo;

    private String link_github;

    @Column(nullable = false)
    @NotNull
    private Integer usuario_id;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    public proyectoCardDto() {
    }

    public proyectoCardDto(String titulo, String descripcion, String public_id, String url_imagen, Boolean estado, String link_demo, String link_github, Integer usuario_id, LocalDateTime createdAt, LocalDateTime editedAt) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.public_id = public_id;
        this.url_imagen = url_imagen;
        this.estado = estado;
        this.link_demo = link_demo;
        this.link_github = link_github;
        this.usuario_id = usuario_id;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }
}
