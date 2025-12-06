package com.gimnasio.servicio_rrhh.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gimnasio.servicio_rrhh.dto.EstadisticasDTO;
import com.gimnasio.servicio_rrhh.dto.PersonalDTO;
import com.gimnasio.servicio_rrhh.model.Personal;
import com.gimnasio.servicio_rrhh.service.RrhhService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/rrhh")
@RequiredArgsConstructor
@Slf4j
public class RrhhController {

    private final RrhhService rrhhService;

    /**
     * Crear nuevo personal
     */
    @PostMapping("/personal")
    public ResponseEntity<PersonalDTO> crearPersonal(@Valid @RequestBody Personal personal) {
        log.info("POST /api/rrhh/personal - Creando personal: {}", personal.getNombre());
        PersonalDTO nuevoPersonal = rrhhService.crearPersonal(personal);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPersonal);
    }

    /**
     * Obtener personal por ID
     */
    @GetMapping("/personal/{id}")
    public ResponseEntity<PersonalDTO> obtenerPersonal(@PathVariable Long id) {
        log.info("GET /api/rrhh/personal/{}", id);
        PersonalDTO personal = rrhhService.obtenerPersonalPorId(id);
        return ResponseEntity.ok(personal);
    }

    /**
     * Obtener todo el personal
     */
    @GetMapping("/personal")
    public ResponseEntity<List<PersonalDTO>> obtenerTodoElPersonal() {
        log.info("GET /api/rrhh/personal");
        List<PersonalDTO> personal = rrhhService.obtenerTodoElPersonal();
        log.info("PERSONAL: {}", personal);
        return ResponseEntity.ok(personal);
    }

    /**
     * Obtener personal activo
     */
    @GetMapping("/personal/activos")
    public ResponseEntity<List<PersonalDTO>> obtenerPersonalActivo() {
        log.info("GET /api/rrhh/personal/activos");
        List<PersonalDTO> personal = rrhhService.obtenerPersonalActivo();
        return ResponseEntity.ok(personal);
    }

    /**
     * Obtener personal por departamento
     */
    @GetMapping("/personal/departamento/{departamento}")
    public ResponseEntity<List<PersonalDTO>> obtenerPersonalPorDepartamento(
            @PathVariable String departamento) {
        log.info("GET /api/rrhh/personal/departamento/{}", departamento);
        List<PersonalDTO> personal = rrhhService.obtenerPersonalPorDepartamento(departamento);
        return ResponseEntity.ok(personal);
    }

    /**
     * Obtener personal por puesto
     */
    @GetMapping("/personal/puesto/{puesto}")
    public ResponseEntity<List<PersonalDTO>> obtenerPersonalPorPuesto(@PathVariable String puesto) {
        log.info("GET /api/rrhh/personal/puesto/{}", puesto);
        List<PersonalDTO> personal = rrhhService.obtenerPersonalPorPuesto(puesto);
        return ResponseEntity.ok(personal);
    }

    /**
     * Obtener personal por estado
     */
    @GetMapping("/personal/estado/{estado}")
    public ResponseEntity<List<PersonalDTO>> obtenerPersonalPorEstado(@PathVariable String estado) {
        log.info("GET /api/rrhh/personal/estado/{}", estado);
        List<PersonalDTO> personal = rrhhService.obtenerPersonalPorEstado(estado);
        return ResponseEntity.ok(personal);
    }

    /**
     * Actualizar personal
     */
    @PutMapping("/personal/{id}")
    public ResponseEntity<PersonalDTO> actualizarPersonal(
            @PathVariable Long id,
            @RequestBody Personal personal) {
        log.info("PUT /api/rrhh/personal/{}", id);
        PersonalDTO personalActualizado = rrhhService.actualizarPersonal(id, personal);
        return ResponseEntity.ok(personalActualizado);
    }

    /**
     * Eliminar personal
     */
    @DeleteMapping("/personal/{id}")
    public ResponseEntity<Map<String, String>> eliminarPersonal(@PathVariable Long id) {
        log.info("DELETE /api/rrhh/personal/{}", id);
        rrhhService.eliminarPersonal(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Personal eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

    /**
     * Cambiar estado del personal
     */
    @PatchMapping("/personal/{id}/estado")
    public ResponseEntity<Map<String, String>> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        log.info("PATCH /api/rrhh/personal/{}/estado", id);
        String nuevoEstado = request.get("estado");
        rrhhService.cambiarEstado(id, nuevoEstado);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Estado actualizado exitosamente");
        return ResponseEntity.ok(response);
    }

    /**
     * Buscar personal por keyword
     */
    @GetMapping("/personal/buscar")
    public ResponseEntity<List<PersonalDTO>> buscarPersonal(
            @RequestParam String keyword) {
        log.info("GET /api/rrhh/personal/buscar?keyword={}", keyword);
        List<PersonalDTO> personal = rrhhService.buscarPersonal(keyword);
        return ResponseEntity.ok(personal);
    }

    /**
     * Obtener estadísticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasDTO> obtenerEstadisticas() {
        log.info("GET /api/rrhh/estadisticas");
        EstadisticasDTO estadisticas = rrhhService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }
    /**
     * Obtener contrataciones recientes
     */
    @GetMapping("/personal/recientes")
    public ResponseEntity<List<PersonalDTO>> obtenerContratacionesRecientes(
            @RequestParam(defaultValue = "3") int meses) {
        log.info("GET /api/rrhh/personal/recientes?meses={}", meses);
        List<PersonalDTO> personal = rrhhService.obtenerContratacionesRecientes(meses);
        return ResponseEntity.ok(personal);
    }/**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "servicio-rrhh");
        return ResponseEntity.ok(response);
    }
}