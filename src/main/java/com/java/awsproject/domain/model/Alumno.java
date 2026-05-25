package com.java.awsproject.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "alumnos")
public class Alumno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private String fotoPerfilUrl;

    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
