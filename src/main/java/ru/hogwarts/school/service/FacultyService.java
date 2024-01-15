package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface FacultyService {
    Faculty createFaculty(Faculty faculty);
    Faculty findFaculty(Long id);
    Faculty editFaculty(Faculty faculty);
    void deleteFaculty(Long id);
    Collection<Faculty> findByColor(String color);

    Collection<Faculty> findByNameOrColor(String name, String color);

    Collection<Faculty> getAllFaculties();

    Collection<Student> findStudentsOfFaculty(Long id);
}
