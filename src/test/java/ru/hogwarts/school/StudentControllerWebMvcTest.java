package ru.hogwarts.school;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentServiceImpl studentService;

    @SpyBean
    private StudentController studentController;

    @Test
    void testGetStudent() throws Exception {
        Long id = 1L;
        String name = "Maria";
        int age = 19;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", id);
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/student/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void testGetAllStudents() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Maria");
        student1.setAge(19);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Sofia");
        student2.setAge(15);

        List<Student> students = new ArrayList<>(List.of(
                student1,
                student2
        ));

        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/findAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(students.size()));

    }

    @Test
    void testGetStudentsByAge() throws Exception {
        Student student1 = new Student();
        student1.setId(3L);
        student1.setName("Alex");
        student1.setAge(22);

        Student student2 = new Student();
        student2.setId(4L);
        student2.setName("Peter");
        student2.setAge(22);

        List<Student> students = new ArrayList<>(List.of(
                student1,
                student2
        ));

        when(studentRepository.findByAge(any(Integer.class))).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?age=22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(students.size()));

    }

    @Test
    void testGetStudentsByAgeBetween() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Maria");
        student1.setAge(19);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Sofia");
        student2.setAge(15);

        List<Student> students = new ArrayList<>(List.of(
                student1,
                student2
        ));

        when(studentRepository.findByAgeBetween(anyInt(), anyInt())).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/findAllStudentsByAgeBetween?min=14&max=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(students.size()));

    }

    @Test
    void testGetFacultyOfStudent() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Faculty of Economics");
        faculty.setColor("green");

        Student student = new Student();
        student.setId(5L);
        student.setName("Maria");
        student.setFaculty(faculty);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.getReferenceById(anyLong())).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/findFaculty/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Faculty of Economics"))
                .andExpect(jsonPath("$.color").value("green"));

    }

    @Test
    void testCreateStudent() throws Exception {
        Long id = 1L;
        String name = "Maria";
        int age = 19;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", id);
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void testEditStudent() throws Exception {
        Long id = 1L;
        String name = "Maria";
        int age = 19;
        int newAge = 22;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", id);
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        Student newStudent = new Student();
        newStudent.setId(student.getId());
        newStudent.setName(student.getName());
        newStudent.setAge(newAge);

        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(newAge));
    }

    @Test
    void testDeleteStudent() throws Exception {
        Long id = 1L;
        String name = "Maria";
        int age = 19;

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testSum1() {
        Stream.iterate(1, a -> a +1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b );
    }

    @Test
    void testSum2() {
        Stream.iterate(1, a -> a +1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b );
    }

    @Test
    void testSum3() {
        Stream.iterate(1, a -> a +1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b );
    }

    @Test
    void testSum4() {
        Stream.iterate(1, a -> a +1)
                .limit(1_000_000)
                .mapToInt(i -> i)
                .sum();
    }

}
