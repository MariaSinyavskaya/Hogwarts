package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyServiceImpl facultyServiceImpl;

    public FacultyController(FacultyServiceImpl facultyServiceImpl) {
        this.facultyServiceImpl = facultyServiceImpl;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyServiceImpl.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("findAllFaculties")
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyServiceImpl.getAllFaculties());
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createFaculty = facultyServiceImpl.createFaculty(faculty);
        return ResponseEntity.ok(createFaculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty editFaculty = facultyServiceImpl.editFaculty(faculty);
        if (editFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyServiceImpl.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getFacultiesByColor(
            @RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyServiceImpl.findByColor(color));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("findAllFacultiesByNameOrColor")
    public ResponseEntity<Collection<Faculty>> getAllFacultiesByNameOrColor(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String name) {
        if ((name != null && !name.isBlank()) || (color != null && !color.isBlank())) {
            return ResponseEntity.ok(facultyServiceImpl.findByNameOrColor(name, color));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("findStudents/{id}")
    public ResponseEntity<Collection<Student>> getStudentsOfFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyServiceImpl.findStudentsOfFaculty(id));
    }


    @GetMapping("get-the-longest-name")
    public ResponseEntity<String> getTheLongestName() {
        return ResponseEntity.ok(facultyServiceImpl.getTheLongestName());
    }
}
