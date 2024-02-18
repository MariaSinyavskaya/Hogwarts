package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentServiceImpl studentServiceImpl;

    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentServiceImpl.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("findAllStudents")
    public ResponseEntity<Collection<Student>> getAllStudent() {
        return ResponseEntity.ok(studentServiceImpl.getAllStudents());
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createStudent = studentServiceImpl.createStudent(student);
        return ResponseEntity.ok(createStudent);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student editStudent = studentServiceImpl.editStudent(student);
        if (editStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentServiceImpl.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getStudentsByAge(
            @RequestParam(required = false) int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentServiceImpl.findByAge(age));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("findAllStudentsByAgeBetween")
    public ResponseEntity<Collection<Student>> getStudentsByAgeBetween(
            @RequestParam int min,
            @RequestParam int max) {
        if (min > 0 & max < 100) {
            return ResponseEntity.ok(studentServiceImpl.findByAgeBetween(min, max));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("findFaculty/{id}")
    public ResponseEntity<Faculty> getFacultyOfStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentServiceImpl.findFacultyOfStudent(id));
    }

    @GetMapping("count-of-student")
    public int getCountOfStudents() {
        return studentServiceImpl.getCountOfStudents();
    }

    @GetMapping("avg-of-students-age")
    public double getAvgOfStudentsAge() {
        return studentServiceImpl.getAvgOfStudentsAge();
    }

    @GetMapping("five-last-students")
    public List<Student> getFiveLastStudents() {
        return studentServiceImpl.getFiveLastStudents();
    }

    @GetMapping("get-names-starts-with-A")
    public ResponseEntity<List<String>> getNamesStartsWithA() {
        return ResponseEntity.ok(studentServiceImpl.getNamesStartsWithA());
    }

    @GetMapping("get-average-of-age")
    public Double getAverageOfAge() {
        return studentServiceImpl.getAverageOfAge();
    }

    @GetMapping("sum")
    public Integer sum() {
        return studentServiceImpl.sum();
    }

    @GetMapping("print-parallel")
    public ResponseEntity<Void> printParallel() {
        studentServiceImpl.printParallel();
        return ResponseEntity.ok().build();
    }

    @GetMapping("print-synchronized")
    public ResponseEntity<Void> printSynchronized() {
        studentServiceImpl.printSynchronized();
        return ResponseEntity.ok().build();
    }
}
