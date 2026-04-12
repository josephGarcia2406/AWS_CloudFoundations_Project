package com.java.awsproject.service;

import com.java.awsproject.domain.model.Alumno;
import org.springframework.stereotype.Service;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AlumnoService {
    // Array en memoria para guardar los datos
    private final List<Alumno> alumnos = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<Alumno> findAll() {
        return alumnos;
    }

    public Optional<Alumno> findById(Long id) {
        return alumnos.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public Alumno save(Alumno alumno) {
        alumno.setId(counter.getAndIncrement());
        alumnos.add(alumno);
        return alumno;
    }

    public Optional<Alumno> update(Long id, Alumno alumnoActualizado) {
        return findById(id).map(alumno -> {
            alumno.setNombres(alumnoActualizado.getNombres());
            alumno.setApellidos(alumnoActualizado.getApellidos());
            alumno.setMatricula(alumnoActualizado.getMatricula());
            alumno.setPromedio(alumnoActualizado.getPromedio());
            return alumno;
        });
    }

    public boolean delete(Long id) {
        return alumnos.removeIf(a -> a.getId().equals(id));
    }
}
