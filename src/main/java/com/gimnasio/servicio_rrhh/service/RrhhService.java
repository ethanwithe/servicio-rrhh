package com.gimnasio.servicio_rrhh.service;

import java.util.List;

import com.gimnasio.servicio_rrhh.dto.EstadisticasDTO;
import com.gimnasio.servicio_rrhh.dto.PersonalDTO;
import com.gimnasio.servicio_rrhh.model.Personal;

public interface RrhhService {

    PersonalDTO crearPersonal(Personal personal);

    PersonalDTO obtenerPersonalPorId(Long id);

    List<PersonalDTO> obtenerTodoElPersonal();

    List<PersonalDTO> obtenerPersonalActivo();

    List<PersonalDTO> obtenerPersonalPorDepartamento(String departamento);

    List<PersonalDTO> obtenerPersonalPorPuesto(String puesto);

    List<PersonalDTO> obtenerPersonalPorEstado(String estado);

    PersonalDTO actualizarPersonal(Long id, Personal personal);

    void eliminarPersonal(Long id);

    void cambiarEstado(Long id, String nuevoEstado);

    List<PersonalDTO> buscarPersonal(String keyword);

    EstadisticasDTO obtenerEstadisticas();

    List<PersonalDTO> obtenerContratacionesRecientes(int meses);
}