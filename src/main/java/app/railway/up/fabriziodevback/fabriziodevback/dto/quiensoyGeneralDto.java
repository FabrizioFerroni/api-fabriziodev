package app.railway.up.fabriziodevback.fabriziodevback.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class quiensoyGeneralDto {

    @NotNull
    @Column(name = "descripcion", nullable = false, length = 1024)
    private String descripcion;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Column(name="descripcion_year", nullable=false, length=1024)
    private String descYear;

    @Column(nullable = false)
    @NotNull
    private Integer usuario_id;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    public quiensoyGeneralDto() {
    }

    public quiensoyGeneralDto(String descripcion, Integer year, String descYear, Integer usuario_id, LocalDateTime createdAt, LocalDateTime editedAt) {
        this.descripcion = descripcion;
        this.year = year;
        this.descYear = descYear;
        this.usuario_id = usuario_id;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }
}
