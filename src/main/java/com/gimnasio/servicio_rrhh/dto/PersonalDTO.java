package com.gimnasio.servicio_rrhh.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import com.gimnasio.servicio_rrhh.model.Personal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDTO {
    private Long id;
    private String nombre;
    private String puesto;
    private String departamento;
    private String email;
    private String telefono;
    private LocalDate fechaIngreso;
    private String estado;
    private String direccion;
    private String documento;
    private LocalDate fechaNacimiento;
    private String genero;
    private Integer antiguedad; // En años
    private Integer edad;
    private LocalDateTime fechaRegistro;

    public static PersonalDTO fromEntity(Personal personal) {
        PersonalDTO dto = PersonalDTO.builder()
            .id(personal.getId())
            .nombre(personal.getNombre())
            .puesto(personal.getPuesto())
            .departamento(personal.getDepartamento())
            .email(personal.getEmail())
            .telefono(personal.getTelefono())
            .fechaIngreso(personal.getFechaIngreso())
            .estado(personal.getEstado())
            .direccion(personal.getDireccion())
            .documento(personal.getDocumento())
            .fechaNacimiento(personal.getFechaNacimiento())
            .genero(personal.getGenero())
            .fechaRegistro(personal.getFechaRegistro())
            .build();

        // Calcular antigüedad
        if (personal.getFechaIngreso() != null) {
            Period period = Period.between(personal.getFechaIngreso(), LocalDate.now());
            dto.setAntiguedad(period.getYears());
        }

        // Calcular edad
        if (personal.getFechaNacimiento() != null) {
            Period period = Period.between(personal.getFechaNacimiento(), LocalDate.now());
            dto.setEdad(period.getYears());
        }

        return dto;
    }
}