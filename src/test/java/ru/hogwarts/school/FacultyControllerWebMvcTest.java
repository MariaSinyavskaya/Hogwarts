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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @SpyBean
    private FacultyController facultyController;

    @Test
    void testGetFaculty() throws Exception {
        Long id = 1L;
        String name = "Faculty of Mathematics";
        String color = "blue";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + faculty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void testGetAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Faculty of Mathematics");
        faculty1.setColor("blue");

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Faculty of Economics");
        faculty2.setColor("green");

        List<Faculty> faculties = new ArrayList<>(List.of(
                faculty1,
                faculty2
        ));

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/findAllFaculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(faculties.size()));

    }

    @Test
    void testGetFacultiesByColor() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Faculty of Mathematics");
        faculty1.setColor("blue");

        List<Faculty> faculties = new ArrayList<>(List.of(
                faculty1
        ));

        when(facultyRepository.findByColor(anyString())).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=blue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(faculties.size()));

    }

    @Test
    void testGetAllFacultiesByNameOrColor() throws Exception {
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Faculty of Mathematics");
        faculty1.setColor("blue");

        List<Faculty> faculties = new ArrayList<>(List.of(
                faculty1
        ));

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty1);
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(anyString(), anyString())).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/findAllFacultiesByNameOrColor?name=Faculty of Mathematics" +
                                "&color=blue")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(faculties.size()));

    }

    @Test
    void testGetStudentsOfFaculty() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Maria");
        student.setAge(19);

        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Faculty of Mathematics");
        faculty1.setColor("blue");
        faculty1.setStudents(List.of(student));

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty1);
        when(facultyRepository.getReferenceById(anyLong()).getStudents()).thenReturn(faculty1.getStudents());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/findStudents/" + faculty1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(faculty1.getStudents().size()));

    }

    @Test
    void testCreateFaculty() throws Exception {
        Long id = 1L;
        String name = "Faculty";
        String color = "black";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void testEditStudent() throws Exception {
        Long id = 1L;
        String name = "Faculty";
        String color = "black";
        String newColor = "white";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        Faculty newFaculty = new Faculty();
        newFaculty.setId(id);
        newFaculty.setName(name);
        newFaculty.setColor(newColor);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(newFaculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(newColor));
    }

    @Test
    void testDeleteStudent() throws Exception {
        Long id = 1L;
        String name = "Faculty";
        String color = "black";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
