package com.universidad.patrones.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universidad.patrones.dto.LibroRequestDTO;
import com.universidad.patrones.dto.LibroResponseDTO;
import com.universidad.patrones.mapper.LibroMapper;
import com.universidad.patrones.model.Libro;
import com.universidad.patrones.service.LibroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/libros")
public class LibroControllerV2 {

    private final LibroService service;
    private final LibroMapper mapper;

    public LibroControllerV2(LibroService service, LibroMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<LibroResponseDTO> listar() {
        return service.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<LibroResponseDTO> crear(@RequestBody @Valid LibroRequestDTO dto) {
        Libro libro = mapper.toEntity(dto);
        Libro guardado = service.save(libro);
        return ResponseEntity.status(201)
                .body(mapper.toResponse(guardado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtener(@PathVariable Long id) {
    Libro libro = service.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Libro no encontrado: " + id));

    return ResponseEntity.ok(mapper.toResponse(libro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}