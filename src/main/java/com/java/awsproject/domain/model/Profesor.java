package com.java.awsproject.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "profesores")
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(Integer numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getHorasClase() {
        return horasClase;
    }

    public void setHorasClase(Integer horasClase) {
        this.horasClase = horasClase;
    }
}
