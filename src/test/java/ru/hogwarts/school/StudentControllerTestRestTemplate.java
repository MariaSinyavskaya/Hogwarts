package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void contextLoads() throws Exception{
        assertThat(studentController).isNotNull();
    }

    @Test
    void testGetAllStudents() throws Exception {
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/student/findAllStudents", String.class))
                .isNotNull();
    }

    @Test
    void testGetStudent1() throws Exception {
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/student/4", String.class))
                .isNotNull();
    }

    @Test
    void testGetStudent2() throws Exception {
        Student student = new Student();
        student.setName("Test name");
        studentRepository.save(student);

        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/student/" + student.getId(), Student.class))
                .isEqualTo(student);
    }

    @Test
    void testGetStudentByAge() throws Exception {
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/student?age=14", String.class))
                .isNotNull();
    }

    @Test
    void testGetStudentsByAgeBetween() throws Exception {
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/student/findAllStudentsByAgeBetween?min=10&max=16", Collection.class))
                .isNotNull();
    }

    @Test
    void testGetFacultyOfStudent() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Faculty of Economics");
        faculty.setColor("green");

        Student student = new Student();
        student.setName("Maria");
        student.setFaculty(faculty);

        facultyRepository.save(faculty);
        studentRepository.save(student);

        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/student/findFaculty/" + student.getId(),Faculty.class))
                .isEqualTo(faculty);
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setName("Sofia");
        studentRepository.save(student);

        assertThat(this.restTemplate
                .postForObject("http://localhost:" + port + "/student",student, Student.class))
                .isEqualTo(student);
    }

    @Test
    void testEditStudent() throws Exception{
        Student student = new Student();
        student.setName("Alex");
        student.setAge(25);
        studentRepository.save(student);

        Student newStudent = new Student();
        newStudent.setId(student.getId());
        newStudent.setName("Alex");
        newStudent.setAge(26);

        restTemplate.put("http://localhost:" + port + "/student", newStudent);

        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/student/" + student.getId(), Student.class))
                .isEqualTo(newStudent);
    }

    @Test
    void testDeleteStudent() throws Exception{
        Student student = new Student();
        student.setName("Bob");
        student.setAge(20);
        studentRepository.save(student);

        restTemplate.delete("http://localhost:" + port + "/student/" + student.getId());

        ResponseEntity<Void> resp = restTemplate
                .exchange("http://localhost:" + port + "/student/" + student.getId(),
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        Void.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }
}
