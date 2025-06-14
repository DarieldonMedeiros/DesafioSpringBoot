package com.zipdin.avaliacao.dto;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;

public class ReleaseDTO {

    @NotBlank(message = "System é obrigatório")
    private String system;

    @NotBlank(message = "Version é obrigatório")
    private String version;

    @ElementCollection
    private List<String> commits;

    @Lob
    private String notes;

    @NotBlank(message = "User é obrigatório")
    private String user;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system != null ? system.trim() : null;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getCommits() {
        return commits;
    }

    public void setCommits(List<String> commits) {
        this.commits = commits;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
