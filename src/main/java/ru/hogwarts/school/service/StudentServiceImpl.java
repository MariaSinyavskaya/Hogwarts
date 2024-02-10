package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Collection;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    @Override
    public Student findStudent(Long id) {
        logger.info("Was invoked method for find student by id");
        return studentRepository.findById(id).get();
    }

    @Override
    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student by id");
        logger.warn("Student with id = {} was deleted", id);
        studentRepository.deleteById(id);
    }

    @Override
    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find students by age");
        logger.debug("Requesting a collection of students with age {}", age);
        return studentRepository.findByAge(age);
    }

    @Override
    public Collection<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for find students by age between min and max values");
        logger.debug("Requesting a collection of students with ages between {} and {}", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for find all students");
        return studentRepository.findAll();
    }

    @Override
    public Faculty findFacultyOfStudent(Long id) {
        logger.info("Was invoked method for find faculty of student by id");
        logger.debug("Requesting the faculty of student with id = {}", id);
        return studentRepository.getReferenceById(id).getFaculty();
    }

    @Override
    public int getCountOfStudents() {
        logger.info("Was invoked method for get count of student");
        return studentRepository.getCountOfStudents();
    }

    @Override
    public double getAvgOfStudentsAge() {
        logger.info("Was invoked method for get average of student's age");
        return studentRepository.getAvgOfStudentsAge();
    }

    @Override
    public List<Student> getFiveLastStudents() {
        logger.info("Was invoked method for get five last students in db");
        return studentRepository.getFiveLastStudents();
    }
}
