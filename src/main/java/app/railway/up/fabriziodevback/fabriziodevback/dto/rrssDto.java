package app.railway.up.fabriziodevback.fabriziodevback.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class rrssDto {
    @NotNull
    @Column(name="nombre", nullable=false, length=255)
    private String nombre;

    @NotNull
    @Column(name = "icon", nullable = false, length = 1024)
    private String icon;

    @Column(nullable = false)
    @NotNull
    private Integer usuario_id;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    public rrssDto() {
    }

    public rrssDto(String nombre, String icon, Integer usuario_id, LocalDateTime createdAt, LocalDateTime editedAt) {
        this.nombre = nombre;
        this.icon = icon;
        this.usuario_id = usuario_id;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }
}
