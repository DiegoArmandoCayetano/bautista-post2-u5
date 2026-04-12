package com.universidad.patrones.controller;

import com.universidad.patrones.dto.*;
import com.universidad.patrones.mapper.LibroMapper;
import com.universidad.patrones.model.Libro;
import com.universidad.patrones.service.LibroService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        return service.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}