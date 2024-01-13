package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentServiceImpl out;

    private final static Student STUDENT_1 = new Student(1L, "Maria", 19);
    private final static Student STUDENT_2 = new Student(2L, "Nikita", 19);
    private final static Student STUDENT_3 = new Student(3L, "Alex", 20);
    private final static Student STUDENT_4 = new Student(4L, "Sofia", 18);
    private final static long ID = 3L;

    @Test
    void shouldReturnStudentForCreateStudentMethod() {
        when(studentRepository.save(STUDENT_1)).thenReturn(STUDENT_1);
        Student result = out.createStudent(STUDENT_1);
        assertEquals(STUDENT_1, result);
        verify(studentRepository, only()).save(STUDENT_1);
    }

    @Test
    void shouldReturnStudentForFindStudentMethod() {
        when(studentRepository.findById(ID)).thenReturn(Optional.of(STUDENT_3));
        Student result = out.findStudent(ID);
        assertEquals(STUDENT_3, result);
        verify(studentRepository, only()).findById(ID);
    }

    @Test
    void shouldReturnStudentForEditStudentMethod() {
        when(studentRepository.save(STUDENT_1)).thenReturn(STUDENT_1);
        Student result = out.editStudent(STUDENT_1);
        assertEquals(STUDENT_1, result);
        verify(studentRepository, only()).save(STUDENT_1);
    }

    @Test
    void shouldDeleteStudent() {
        doNothing().when(studentRepository).deleteById(ID);
        out.deleteStudent(ID);
        verify(studentRepository, only()).deleteById(ID);
    }

    @Test
    void shouldFindStudentsByAge() {
        List<Student> expectedList = new ArrayList<>(List.of(STUDENT_1, STUDENT_2));
        int age = 19;
        when(studentRepository.findByAge(age)).thenReturn(expectedList);
        assertIterableEquals(expectedList, out.findByAge(age));
        verify(studentRepository, only()).findByAge(age);
    }

    @Test
    void shouldGetAllStudents() {
        List<Student> expectedList = new ArrayList<>(List.of(
                STUDENT_1, STUDENT_2, STUDENT_3, STUDENT_4));
        when(studentRepository.findAll()).thenReturn(expectedList);
        assertIterableEquals(expectedList, out.getAllStudents());
        verify(studentRepository, only()).findAll();
    }

    @Test
    void shouldThrowNotFoundException() {
        long id = 5L;
        when(studentRepository.findById(id)).thenThrow(HttpClientErrorException.NotFound.class);
        assertThrows(HttpClientErrorException.NotFound.class,
                ()-> out.findStudent(id));
    }
}