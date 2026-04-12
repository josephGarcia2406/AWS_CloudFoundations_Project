package com.java.awsproject.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Alumno {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotBlank(message = "La matrícula no puede estar vacía")
    private String matricula;

    @NotNull(message = "El promedio es obligatorio")
    @Min(value = 0, message = "El promedio no puede ser negativo")
    private Double promedio;

}
