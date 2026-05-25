package com.java.awsproject.controller;

import com.java.awsproject.domain.model.Alumno;
import com.java.awsproject.domain.model.Session;
import com.java.awsproject.service.AlumnoService;
import com.java.awsproject.service.DynamoDbService;
import com.java.awsproject.service.S3Service;
import com.java.awsproject.service.SnsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final S3Service s3Service;
    private final SnsService snsService;
    private final DynamoDbService dynamoDbService;

    public AlumnoController(AlumnoService alumnoService, S3Service s3Service, SnsService snsService, DynamoDbService dynamoDbService){
        this.alumnoService = alumnoService;
        this.s3Service = s3Service;
        this.snsService = snsService;
        this.dynamoDbService = dynamoDbService;
    }

    // GET /alumnos
    @GetMapping
    public ResponseEntity<List<Alumno>> getAll() {
        return ResponseEntity.ok(alumnoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getById(@PathVariable Long id) {
        return alumnoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /alumnos
    @PostMapping
    public ResponseEntity<Alumno> create(@Valid @RequestBody Alumno alumno) {
        alumno.setId(null);
        Alumno nuevoAlumno = alumnoService.save(alumno);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlumno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alumno> update(@PathVariable Long id, @Valid @RequestBody Alumno alumno)  {
        return alumnoService.update(id, alumno)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Alumno> delete(@PathVariable Long id) {
        if(alumnoService.delete(id))
            return ResponseEntity.ok().build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // POST /alumnos/{id}/fotoPerfil
    @PostMapping(value = "/{id}/fotoPerfil", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> uploadFotoPerfil(
            @PathVariable Long id,
            @RequestParam(value = "fotoPerfil", required = false) MultipartFile fotoPerfil,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        
        MultipartFile activeFile = (fotoPerfil != null) ? fotoPerfil : file;
        if (activeFile == null || activeFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return alumnoService.findById(id).map(alumno -> {
            try {
                String fotoUrl = s3Service.uploadFile(activeFile);
                alumno.setFotoPerfilUrl(fotoUrl);
                alumnoService.save(alumno); // Persistir en RDS MySQL
                
                Map<String, String> response = new HashMap<>();
                response.put("fotoPerfilUrl", fotoUrl);
                return ResponseEntity.ok(response);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Map<String, String>>build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // POST /alumnos/{id}/email
    @PostMapping("/{id}/email")
    public ResponseEntity<Map<String, String>> sendEmailNotification(@PathVariable Long id) {
        return alumnoService.findById(id).map(alumno -> {
            String subject = "Reporte de Calificaciones - " + alumno.getNombres() + " " + alumno.getApellidos();
            String body = String.format(
                    "Información del Alumno:\n" +
                    "Nombre: %s %s\n" +
                    "Matrícula: %s\n" +
                    "Promedio de Calificaciones: %.2f\n",
                    alumno.getNombres(),
                    alumno.getApellidos(),
                    alumno.getMatricula(),
                    alumno.getPromedio()
            );

            String messageId = snsService.publishMessage(subject, body);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("messageId", messageId);
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }

    // POST /alumnos/{id}/session/login
    @PostMapping("/{id}/session/login")
    public ResponseEntity<Session> login(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body,
            @RequestParam(value = "password", required = false) String passwordParam) {
        
        String password = null;
        if (body != null && body.containsKey("password")) {
            password = body.get("password");
        } else if (passwordParam != null) {
            password = passwordParam;
        }

        if (password == null) {
            return ResponseEntity.badRequest().build();
        }

        final String finalPassword = password;
        return alumnoService.findById(id).map(alumno -> {
            // Verificar si las contraseñas coinciden
            if (alumno.getPassword() != null && alumno.getPassword().equals(finalPassword)) {
                Session session = new Session();
                session.setId(UUID.randomUUID().toString());
                session.setFecha(System.currentTimeMillis() / 1000L);
                session.setAlumnoId(alumno.getId());
                session.setActive(true);
                session.setSessionString(generateSessionString());

                dynamoDbService.saveSession(session);
                return ResponseEntity.ok(session);
            }
            return ResponseEntity.badRequest().<Session>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // POST /alumnos/{id}/session/verify
    @PostMapping("/{id}/session/verify")
    public ResponseEntity<Void> verify(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body,
            @RequestParam(value = "sessionString", required = false) String sessionStringParam) {
        
        String sessionString = null;
        if (body != null && body.containsKey("sessionString")) {
            sessionString = body.get("sessionString");
        } else if (sessionStringParam != null) {
            sessionString = sessionStringParam;
        }

        if (sessionString == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Session> sessionOpt = dynamoDbService.findSessionByString(sessionString);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            if (session.getAlumnoId().equals(id) && session.getActive()) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    // POST /alumnos/{id}/session/logout
    @PostMapping("/{id}/session/logout")
    public ResponseEntity<Void> logout(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body,
            @RequestParam(value = "sessionString", required = false) String sessionStringParam) {
        
        String sessionString = null;
        if (body != null && body.containsKey("sessionString")) {
            sessionString = body.get("sessionString");
        } else if (sessionStringParam != null) {
            sessionString = sessionStringParam;
        }

        if (sessionString == null) {
            return ResponseEntity.badRequest().build();
        }

        if (dynamoDbService.deactivateSession(sessionString)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    private String generateSessionString() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(128);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 128; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
