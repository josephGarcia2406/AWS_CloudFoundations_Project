package com.java.awsproject.domain.model;

public class Session {
    private String id;
    private Long fecha; // Unix timestamp (number)
    private Long alumnoId; // (number)
    private Boolean active = true; // (boolean)
    private String sessionString; // (string)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSessionString() {
        return sessionString;
    }

    public void setSessionString(String sessionString) {
        this.sessionString = sessionString;
    }
}
