package com.gimnasio.servicio_rrhh.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gimnasio.servicio_rrhh.model.Personal;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long> {

    Optional<Personal> findByEmail(String email);

    List<Personal> findByDepartamento(String departamento);

    List<Personal> findByPuesto(String puesto);

    List<Personal> findByEstado(String estado);

    boolean existsByEmail(String email);

    boolean existsByDocumento(String documento);

    @Query("SELECT p FROM Personal p WHERE p.estado = 'Activo'")
    List<Personal> findAllActive();

    @Query("SELECT COUNT(p) FROM Personal p WHERE p.estado = 'Activo'")
    long countActivePersonal();

    @Query("SELECT COUNT(p) FROM Personal p WHERE p.departamento = :departamento")
    long countByDepartamento(@Param("departamento") String departamento);

    @Query("SELECT COUNT(p) FROM Personal p WHERE p.estado = :estado")
    long countByEstado(@Param("estado") String estado);

    @Query("SELECT p FROM Personal p WHERE p.fechaIngreso >= :fecha")
    List<Personal> findRecentHires(@Param("fecha") LocalDate fecha);

    @Query("SELECT AVG(p.salario) FROM Personal p WHERE p.estado = 'Activo'")
    Double findAverageSalary();

    @Query("SELECT p FROM Personal p WHERE p.nombre LIKE %:keyword% OR p.puesto LIKE %:keyword% OR p.departamento LIKE %:keyword%")
    List<Personal> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p.departamento, COUNT(p) FROM Personal p GROUP BY p.departamento")
    List<Object[]> countByDepartamentoGrouped();

    @Query("SELECT p.puesto, COUNT(p) FROM Personal p GROUP BY p.puesto")
    List<Object[]> countByPuestoGrouped();

    @Query("SELECT p.estado, COUNT(p) FROM Personal p GROUP BY p.estado")
    List<Object[]> countByEstadoGrouped();
}