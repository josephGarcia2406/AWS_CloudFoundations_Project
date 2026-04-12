package com.java.awsproject.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Profesor {
    private Long id;

    @NotNull(message = "El número de empleado es obligatorio")
    private Integer numeroEmpleado;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotNull(message = "Las horas de clase son obligatorias")
    @Min(value = 0, message = "Las horas no pueden ser negativas")
    private Integer horasClase;

}
