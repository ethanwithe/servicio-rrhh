package com.gimnasio.servicio_rrhh.exception;

public class PersonalNotFoundException extends RuntimeException {
    public PersonalNotFoundException(String message) {
        super(message);
    }

    public PersonalNotFoundException(Long id) {
        super("Personal no encontrado con ID: " + id);
    }
}
