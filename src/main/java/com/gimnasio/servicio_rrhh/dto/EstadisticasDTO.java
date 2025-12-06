package com.gimnasio.servicio_rrhh.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasDTO {
    private Long totalPersonal;
    private Long personalActivo;
    private Long personalVacaciones;
    private Long personalLicencia;
    private Long nuevasContrataciones;
    private Map<String, Long> porDepartamento;
    private Map<String, Long> porPuesto;
    private Map<String, Long> porEstado;
    private Double salarioPromedio;
    private Integer antiguedadPromedio;
}