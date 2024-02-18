package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {
    Student createStudent(Student student);
    Student findStudent(Long id);
    Student editStudent(Student student);
    void deleteStudent(Long id);
    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    Collection<Student> getAllStudents();

    Faculty findFacultyOfStudent(Long id);

    int getCountOfStudents();

    double getAvgOfStudentsAge();

    List<Student> getFiveLastStudents();

    List<String> getNamesStartsWithA();

    double getAverageOfAge();

    int sum();

}
