package com.zipdin.avaliacao.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.zipdin.avaliacao.services.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "releases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "System é obrigatório")
    @Column(nullable = false, length = 255)
    private String system;

    @NotBlank(message = "Version é obrigatório")
    @Column(nullable = false, length = 30)
    private String version;

    @NotEmpty(message = "Commits não pode ser vazio")
    @Convert(converter = StringListConverter.class)
    @Column(name = "commits")
    private List<String> commits;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @NotBlank(message = "User é obrigatório")
    @Size(max = 100, message = "User não pode exceder 100 caracteres")
    @Column(name = "`user`", nullable = false, length = 100)
    private String user;

    @Size(max = 100, message = "User Update não pode exceder 100 caracteres")
    @Column(length = 100)
    private String userUpdate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime releasedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void setSystem(String system) {
        this.system = system != null ? system.trim() : null;
    }

}
