package com.java.awsproject.service;

import com.java.awsproject.domain.model.Profesor;
import com.java.awsproject.repository.ProfesorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    public Optional<Profesor> findById(Long id) {
        return profesorRepository.findById(id);
    }

    public Profesor save(Profesor profesor) {
        if (profesor.getId() != null && profesor.getId() == 0) {
            profesor.setId(null);
        }
        return profesorRepository.save(profesor);
    }

    public Optional<Profesor> update(Long id, Profesor profesorActualizado) {
        return profesorRepository.findById(id).map(profesor -> {
            profesor.setNombres(profesorActualizado.getNombres());
            profesor.setApellidos(profesorActualizado.getApellidos());
            profesor.setNumeroEmpleado(profesorActualizado.getNumeroEmpleado());
            profesor.setHorasClase(profesorActualizado.getHorasClase());
            return profesorRepository.save(profesor);
        });
    }

    public boolean delete(Long id) {
        if (profesorRepository.existsById(id)) {
            profesorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
