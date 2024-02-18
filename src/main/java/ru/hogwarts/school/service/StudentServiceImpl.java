package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentServiceImpl implements StudentService {
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

    @Override
    public List<String> getNamesStartsWithA() {
        logger.info("Was invoked method for get names of students starts with A");
        return studentRepository.findAll()
                .stream()
                .parallel()
                .filter(Objects::nonNull)
                .map(Student::getName)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageOfAge() {
        logger.info("Was invoked method for get average of age");
        return studentRepository.findAll()
                .stream()
                .parallel()
                .filter(Objects::nonNull)
                .map(Student::getAge)
                .mapToDouble(a -> a)
                .average()
                .orElse(0);
    }

    @Override
    public int sum() {
        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        return sum;

        // В классе StudentControllerWebMvcTest провела 4 теста
        // с разными вариантами реализации данного метода,
        // чтобы сравнить время выполнения. Данный вариант оказался лучшим.
        // Использование параллельных стримов затрачивает гораздо больше времени и ресурсов.

    }

    private void print(List<Student> studentList, int index) {
        System.out.println(studentList.get(index).getName());
    }

    private synchronized void printSync(List<Student> studentList, int index) {
        System.out.println(studentList.get(index).getName());
    }

    @Override
    public void printParallel() {
        List<Student> studentList = studentRepository.findAll();

        print(studentList, 0);
        print(studentList, 1);

        new Thread(() -> {
            print(studentList, 2);
            print(studentList, 3);
        }).start();

        new Thread(() -> {
            print(studentList, 4);
            print(studentList, 5);
        }).start();
    }

    @Override
    public void printSynchronized() {
        List<Student> studentList = studentRepository.findAll();

        printSync(studentList, 0);
        printSync(studentList, 1);

        new Thread(() -> {
            printSync(studentList, 2);
            printSync(studentList, 3);
        }).start();

        new Thread(() -> {
            printSync(studentList, 4);
            printSync(studentList, 5);
        }).start();
    }
}
