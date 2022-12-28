package app.railway.up.fabriziodevback.fabriziodevback.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class quiensoyCardDto {
    @NotNull
    @Column(name="titulo", nullable=false, length=255)
    private String titulo;

    private String public_id;

    private String url_imagen;

    @Column(nullable = false)
    @NotNull
    private Integer usuario_id;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    public quiensoyCardDto() {
    }

    public quiensoyCardDto(String titulo, String public_id, String url_imagen, Integer usuario_id, LocalDateTime createdAt, LocalDateTime editedAt) {
        this.titulo = titulo;
        this.public_id = public_id;
        this.url_imagen = url_imagen;
        this.usuario_id = usuario_id;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }
}
