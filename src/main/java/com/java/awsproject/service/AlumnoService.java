package com.java.awsproject.service;

import com.java.awsproject.domain.model.Alumno;
import com.java.awsproject.repository.AlumnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    public List<Alumno> findAll() {
        return alumnoRepository.findAll();
    }

    public Optional<Alumno> findById(Long id) {
        return alumnoRepository.findById(id);
    }

    public Alumno save(Alumno alumno) {
        if (alumno.getId() != null && alumno.getId() == 0) {
            alumno.setId(null);
        }
        return alumnoRepository.save(alumno);
    }

    public Optional<Alumno> update(Long id, Alumno alumnoActualizado) {
        return alumnoRepository.findById(id).map(alumno -> {
            alumno.setNombres(alumnoActualizado.getNombres());
            alumno.setApellidos(alumnoActualizado.getApellidos());
            alumno.setMatricula(alumnoActualizado.getMatricula());
            alumno.setPromedio(alumnoActualizado.getPromedio());
            if (alumnoActualizado.getPassword() != null) {
                alumno.setPassword(alumnoActualizado.getPassword());
            }
            if (alumnoActualizado.getFotoPerfilUrl() != null) {
                alumno.setFotoPerfilUrl(alumnoActualizado.getFotoPerfilUrl());
            }
            return alumnoRepository.save(alumno);
        });
    }

    public boolean delete(Long id) {
        if (alumnoRepository.existsById(id)) {
            alumnoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
