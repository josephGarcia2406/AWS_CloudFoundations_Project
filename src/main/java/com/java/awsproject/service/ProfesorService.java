package com.java.awsproject.service;

import com.java.awsproject.domain.model.Profesor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProfesorService {
    // Array en memoria para guardar los datos
    private final List<Profesor> profesores = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<Profesor> findAll() {
        return profesores;
    }

    public Optional<Profesor> findById(Long id) {
        return profesores.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public Profesor save(Profesor Profesor) {
        if (Profesor.getId() == null || Profesor.getId() == 0) {
            Profesor.setId(counter.getAndIncrement());
        }
        profesores.add(Profesor);
        return Profesor;
    }

    public Optional<Profesor> update(Long id, Profesor ProfesorActualizado) {
        return findById(id).map(Profesor -> {
            Profesor.setNombres(ProfesorActualizado.getNombres());
            Profesor.setApellidos(ProfesorActualizado.getApellidos());
            Profesor.setNumeroEmpleado(ProfesorActualizado.getNumeroEmpleado());
            Profesor.setHorasClase(ProfesorActualizado.getHorasClase());
            return Profesor;
        });
    }

    public boolean delete(Long id) {
        return profesores.removeIf(a -> a.getId().equals(id));
    }
}
