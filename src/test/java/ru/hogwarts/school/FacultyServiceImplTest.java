package ru.hogwarts.school;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceImplTest {

    @Mock
    FacultyRepository facultyRepository;

    @InjectMocks
    FacultyServiceImpl out;

    private final static Faculty FACULTY_1 = new Faculty(1L, "Гриффиндор", "красный");
    private final static Faculty FACULTY_2 = new Faculty(2L, "Слизерин", "зеленый");
    private final static long ID = 2L;

    @Test
    void shouldReturnFacultyForCreateFacultyMethod() {
        when(facultyRepository.save(FACULTY_1)).thenReturn(FACULTY_1);
        Faculty result = out.createFaculty(FACULTY_1);
        assertEquals(FACULTY_1, result);
        verify(facultyRepository, only()).save(FACULTY_1);
    }

    @Test
    void shouldReturnFacultyForFindFacultyMethod() {
        when(facultyRepository.findById(ID)).thenReturn(Optional.of(FACULTY_2));
        Faculty result = out.findFaculty(ID);
        assertEquals(FACULTY_2, result);
        verify(facultyRepository, only()).findById(ID);
    }

    @Test
    void shouldReturnFacultyForEditFacultyMethod() {
        when(facultyRepository.save(FACULTY_1)).thenReturn(FACULTY_1);
        Faculty result = out.editFaculty(FACULTY_1);
        assertEquals(FACULTY_1, result);
        verify(facultyRepository, only()).save(FACULTY_1);
    }

    @Test
    void shouldDeleteFaculty() {
        doNothing().when(facultyRepository).deleteById(ID);
        out.deleteFaculty(ID);
        verify(facultyRepository, only()).deleteById(ID);
    }

    @Test
    void shouldFindFacultyByColor() {
        List<Faculty> expectedList = new ArrayList<>(List.of(FACULTY_1));
        String color = "красный";
        when(facultyRepository.findByColor(color)).thenReturn(expectedList);
        assertIterableEquals(expectedList, out.findByColor(color));
        verify(facultyRepository, only()).findByColor(color);
    }

    @Test
    void shouldGetAllFaculties() {
        List<Faculty> expectedList = new ArrayList<>(List.of(FACULTY_1, FACULTY_2));
        when(facultyRepository.findAll()).thenReturn(expectedList);
        assertIterableEquals(expectedList, out.getAllFaculties());
        verify(facultyRepository, only()).findAll();
    }

    @Test
    void shouldThrowNotFoundException() {
        long id = 5L;
        when(facultyRepository.findById(id)).thenThrow(HttpClientErrorException.NotFound.class);
        assertThrows(HttpClientErrorException.NotFound.class,
                ()-> out.findFaculty(id));
    }
}
