package com.zipdin.avaliacao.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zipdin.avaliacao.dto.GenericResponseDTO;
import com.zipdin.avaliacao.dto.ReleaseDTO;
import com.zipdin.avaliacao.dto.ReleaseResponseDTO;
import com.zipdin.avaliacao.dto.UpdateNotesDTO;
import com.zipdin.avaliacao.entities.ReleaseEntity;
import com.zipdin.avaliacao.repository.ReleaseRepository;
import com.zipdin.avaliacao.services.ReleaseServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/releases")
@Tag(name = "Releases")
public class ReleaseController {

    private final ReleaseServices releaseServices;

    final ReleaseRepository releaseRepository;

    public ReleaseController(ReleaseRepository releaseRepository, ReleaseServices releaseServices) {
        this.releaseRepository = releaseRepository;
        this.releaseServices = releaseServices;
    }

    @PostMapping
    @Operation(summary = "Salvar", description = "Cadastrar novo release")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Release criado com sucesso!"),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
    })
    public ResponseEntity<Object> createRelease(@RequestBody @Valid ReleaseDTO releaseDTO){
        var releaseEntity = new ReleaseEntity();
        BeanUtils.copyProperties(releaseDTO, releaseEntity);
        releaseEntity.setReleasedAt(LocalDateTime.now());
        var savedRelease = releaseServices.saveRelease(releaseEntity);

        // Montando a resposta
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedRelease.getId());
        response.put("message", "Release criado com sucesso!");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Buscar release por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Release listado com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Release não encontrado"),
    })
    public ResponseEntity<ReleaseResponseDTO> getReleaseByID(@PathVariable(value = "id") Long id){
        Optional<ReleaseEntity> releaseEntityOptional = releaseServices.findById(id);
        if (releaseEntityOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ReleaseEntity releaseEntity = releaseEntityOptional.get();
        ReleaseResponseDTO releaseResponseDTO = new ReleaseResponseDTO(
            "Release listado com sucesso!",
            releaseEntity.getId(),
            releaseEntity.getSystem(),
            releaseEntity.getVersion(),
            releaseEntity.getCommits(),
            releaseEntity.getNotes(),
            releaseEntity.getUser(),
            releaseEntity.getUserUpdate(),
            releaseEntity.getReleasedAt()
        );

        return ResponseEntity.status(HttpStatus.OK).body(releaseResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar", description = "Atualizar release")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Release atualizado com sucesso!"),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
    })
    public ResponseEntity<Object> updateReleaseNotes(@PathVariable(value = "id") Long id, @RequestBody @Valid UpdateNotesDTO updateNotesDTO) {
        releaseServices.updateReleaseNotes(id, updateNotesDTO);
        GenericResponseDTO response = new GenericResponseDTO("Release atualizado com sucesso!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar", description = "Deletar release")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Release deletado com sucesso!"),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro inesperado"),
    })
    public ResponseEntity<Object> deleteRelease(@PathVariable(value = "id") Long id){
        releaseServices.deleteRelease(id);
        GenericResponseDTO response = new GenericResponseDTO("Release deletado com sucesso!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Paginação
    @GetMapping
    @Operation(summary = "Listar", description = "Listar releases")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Releases listados com sucesso!"),
    })
    public ResponseEntity<Page<ReleaseEntity>> getAllReleases(@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(releaseServices.findAll(pageable));
    }

}
