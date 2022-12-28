package app.railway.up.fabriziodevback.fabriziodevback.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "boolean default false", nullable = false)
    @NotNull
    private Boolean estado;

    @Column(nullable = false)
    @NotNull
    private Integer usuario_id;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime editedAt;

    public maintenance() {
    }

    public maintenance(int id, Boolean estado, Integer usuario_id, LocalDateTime editedAt) {
        this.id = id;
        this.estado = estado;
        this.usuario_id = usuario_id;
        this.editedAt = editedAt;
    }
}
