package app.railway.up.fabriziodevback.fabriziodevback.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class maintenanceDto {
    @Column(columnDefinition = "boolean default false", nullable = false)
    @NotNull
    private Boolean estado;

    @Column(nullable = false)
    @NotNull
    private Integer usuario_id;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime editedAt;

    public maintenanceDto() {
    }

    public maintenanceDto(Boolean estado, Integer usuario_id, LocalDateTime editedAt) {
        this.estado = estado;
        this.usuario_id = usuario_id;
        this.editedAt = editedAt;
    }
}
