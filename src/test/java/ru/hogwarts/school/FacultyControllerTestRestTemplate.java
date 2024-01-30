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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void contextLoads() throws Exception{
        assertThat(facultyController).isNotNull();
    }

    @Test
    void testGetAllFaculties() throws Exception {
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/faculty/findAllFaculties", String.class))
                .isNotNull();
    }

    @Test
    void testGetFaculty1() throws Exception {
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/faculty/5", String.class))
                .isNotNull();
    }

    @Test
    void testGetFaculty2() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Faculty of Economics");
        faculty.setColor("green");
        facultyRepository.save(faculty);

        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/faculty/" + faculty.getId(), Faculty.class))
                .isEqualTo(faculty);
    }

    @Test
    void testGetFacultiesByColor() throws Exception {
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/faculty/?color=green", String.class))
                .isNotNull();
    }

    @Test
    void testGetAllFacultiesByNameOrColor() {
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port
                        + "/faculty/findAllFacultiesByNameOrColor/?color=green", String.class))
                .isNotNull();
        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port
                        + "/faculty/findAllFacultiesByNameOrColor/?name=slytherin", String.class))
                .isNotNull();
    }

    @Test
    void testGetStudentsOfFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Faculty of Mathematics");
        faculty.setColor("purple");

        Student student = new Student();
        student.setName("Maria");
        student.setFaculty(faculty);

        facultyRepository.save(faculty);
        studentRepository.save(student);

        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port
                        + "/faculty/findStudents/" + faculty.getId(), Collection.class))
                .isNotNull();
    }

    @Test
    void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Faculty of Mathematics");
        facultyRepository.save(faculty);

        assertThat(this.restTemplate
                .postForObject("http://localhost:" + port + "/faculty",faculty, Faculty.class))
                .isEqualTo(faculty);
    }

    @Test
    void testEditFaculty() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setName("Faculty of Mathematics");
        faculty.setColor("purple");
        facultyRepository.save(faculty);

        Faculty newFaculty = new Faculty();
        newFaculty.setId(faculty.getId());
        newFaculty.setName("Faculty of Mathematics");
        newFaculty.setColor("pink");

        restTemplate.put("http://localhost:" + port + "/faculty", newFaculty);

        assertThat(this.restTemplate
                .getForObject("http://localhost:" + port + "/faculty/" + faculty.getId(), Faculty.class))
                .isEqualTo(newFaculty);
    }

    @Test
    void testDeleteFaculty() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setName("Faculty of Mathematics");
        faculty.setColor("purple");
        facultyRepository.save(faculty);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + faculty.getId());

        ResponseEntity<Void> resp = restTemplate
                .exchange("http://localhost:" + port + "/faculty/" + faculty.getId(),
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        Void.class);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }
}
