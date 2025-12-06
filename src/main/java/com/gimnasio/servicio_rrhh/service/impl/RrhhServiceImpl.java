package com.gimnasio.servicio_rrhh.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gimnasio.servicio_rrhh.dto.EstadisticasDTO;
import com.gimnasio.servicio_rrhh.dto.PersonalDTO;
import com.gimnasio.servicio_rrhh.exception.PersonalNotFoundException;
import com.gimnasio.servicio_rrhh.model.Personal;
import com.gimnasio.servicio_rrhh.repository.PersonalRepository;
import com.gimnasio.servicio_rrhh.service.RrhhService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RrhhServiceImpl implements RrhhService {

    private final PersonalRepository personalRepository;

    @Override
    public PersonalDTO crearPersonal(Personal personal) {
        log.info("Creando nuevo personal: {}", personal.getNombre());

        if (personalRepository.existsByEmail(personal.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        if (personal.getDocumento() != null &&
            personalRepository.existsByDocumento(personal.getDocumento())) {
            throw new IllegalArgumentException("El documento ya está registrado");
        }

        Personal nuevoPersonal = personalRepository.save(personal);
        log.info("Personal creado exitosamente con ID: {}", nuevoPersonal.getId());

        return PersonalDTO.fromEntity(nuevoPersonal);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonalDTO obtenerPersonalPorId(Long id) {
        log.info("Buscando personal con ID: {}", id);
        Personal personal = personalRepository.findById(id)
            .orElseThrow(() -> new PersonalNotFoundException(id));
        return PersonalDTO.fromEntity(personal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonalDTO> obtenerTodoElPersonal() {
        log.info("Obteniendo todo el personal");
        return personalRepository.findAll().stream()
            .map(PersonalDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonalDTO> obtenerPersonalActivo() {
        log.info("Obteniendo personal activo");
        return personalRepository.findAllActive().stream()
            .map(PersonalDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonalDTO> obtenerPersonalPorDepartamento(String departamento) {
        log.info("Obteniendo personal del departamento: {}", departamento);
        return personalRepository.findByDepartamento(departamento).stream()
            .map(PersonalDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonalDTO> obtenerPersonalPorPuesto(String puesto) {
        log.info("Obteniendo personal por puesto: {}", puesto);
        return personalRepository.findByPuesto(puesto).stream()
            .map(PersonalDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonalDTO> obtenerPersonalPorEstado(String estado) {
        log.info("Obteniendo personal por estado: {}", estado);
        return personalRepository.findByEstado(estado).stream()
            .map(PersonalDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public PersonalDTO actualizarPersonal(Long id, Personal personalActualizado) {
        log.info("Actualizando personal con ID: {}", id);

        Personal personal = personalRepository.findById(id)
            .orElseThrow(() -> new PersonalNotFoundException(id));

        // Actualizar campos
        if (personalActualizado.getNombre() != null) {
            personal.setNombre(personalActualizado.getNombre());
        }
        if (personalActualizado.getPuesto() != null) {
            personal.setPuesto(personalActualizado.getPuesto());
        }
        if (personalActualizado.getDepartamento() != null) {
            personal.setDepartamento(personalActualizado.getDepartamento());
        }
        if (personalActualizado.getEmail() != null) {
            personal.setEmail(personalActualizado.getEmail());
        }
        if (personalActualizado.getTelefono() != null) {
            personal.setTelefono(personalActualizado.getTelefono());
        }
        if (personalActualizado.getDireccion() != null) {
            personal.setDireccion(personalActualizado.getDireccion());
        }
        if (personalActualizado.getSalario() != null) {
            personal.setSalario(personalActualizado.getSalario());
        }
        if (personalActualizado.getEstado() != null) {
            personal.setEstado(personalActualizado.getEstado());
        }

        Personal personalGuardado = personalRepository.save(personal);
        log.info("Personal actualizado exitosamente");

        return PersonalDTO.fromEntity(personalGuardado);
    }

    @Override
    public void eliminarPersonal(Long id) {
        log.info("Eliminando personal con ID: {}", id);
        if (!personalRepository.existsById(id)) {
            throw new PersonalNotFoundException(id);
        }
        personalRepository.deleteById(id);
        log.info("Personal eliminado exitosamente");
    }

    @Override
    public void cambiarEstado(Long id, String nuevoEstado) {
        log.info("Cambiando estado del personal ID {} a: {}", id, nuevoEstado);
        Personal personal = personalRepository.findById(id)
            .orElseThrow(() -> new PersonalNotFoundException(id));
        personal.setEstado(nuevoEstado);
        personalRepository.save(personal);
        log.info("Estado cambiado exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonalDTO> buscarPersonal(String keyword) {
        log.info("Buscando personal con keyword: {}", keyword);
        return personalRepository.searchByKeyword(keyword).stream()
            .map(PersonalDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EstadisticasDTO obtenerEstadisticas() {
        log.info("Obteniendo estadísticas de RRHH");

        long totalPersonal = personalRepository.count();
        long personalActivo = personalRepository.countActivePersonal();
        long personalVacaciones = personalRepository.countByEstado("Vacaciones");
        long personalLicencia = personalRepository.countByEstado("Licencia");

        // Contrataciones de los últimos 3 meses
        LocalDate tresMesesAtras = LocalDate.now().minusMonths(3);
        long nuevasContrataciones = personalRepository.findRecentHires(tresMesesAtras).size();

        // Distribución por departamento
        Map<String, Long> porDepartamento = new HashMap<>();
        List<Object[]> departamentos = personalRepository.countByDepartamentoGrouped();
        for (Object[] row : departamentos) {
            porDepartamento.put((String) row[0], (Long) row[1]);
        }

        // Distribución por puesto
        Map<String, Long> porPuesto = new HashMap<>();
        List<Object[]> puestos = personalRepository.countByPuestoGrouped();
        for (Object[] row : puestos) {
            porPuesto.put((String) row[0], (Long) row[1]);
        }

        // Distribución por estado
        Map<String, Long> porEstado = new HashMap<>();
        List<Object[]> estados = personalRepository.countByEstadoGrouped();
        for (Object[] row : estados) {
            porEstado.put((String) row[0], (Long) row[1]);
        }

        Double salarioPromedio = personalRepository.findAverageSalary();

        return EstadisticasDTO.builder()
            .totalPersonal(totalPersonal)
            .personalActivo(personalActivo)
            .personalVacaciones(personalVacaciones)
            .personalLicencia(personalLicencia)
            .nuevasContrataciones(nuevasContrataciones)
            .porDepartamento(porDepartamento)
            .porPuesto(porPuesto)
            .porEstado(porEstado)
            .salarioPromedio(salarioPromedio)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonalDTO> obtenerContratacionesRecientes(int meses) {
        log.info("Obteniendo contrataciones de los últimos {} meses", meses);
        LocalDate fechaLimite = LocalDate.now().minusMonths(meses);
        return personalRepository.findRecentHires(fechaLimite).stream()
            .map(PersonalDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
