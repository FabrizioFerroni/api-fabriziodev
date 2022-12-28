package app.railway.up.fabriziodevback.fabriziodevback.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class proyectoGeneralDto {
    @NotNull
    @Column(name="descripcion", nullable=false, length=1024)
    private String descripcion;

    @Column(nullable = false)
    @NotNull
    private Integer usuario_id;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    public proyectoGeneralDto() {
    }

    public proyectoGeneralDto(String descripcion, Integer usuario_id, LocalDateTime createdAt, LocalDateTime editedAt) {
        this.descripcion = descripcion;
        this.usuario_id = usuario_id;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }
}
