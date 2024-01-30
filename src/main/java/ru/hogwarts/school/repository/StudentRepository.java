package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    @Query(value = "select count(*) from student", nativeQuery = true)
    int getCountOfStudents();

    @Query(value = "select avg(age) from student", nativeQuery = true)
    double getAvgOfStudentsAge();

    @Query(value = "select * from student order by id limit 5 offset(select count(*) from student) - 5", nativeQuery = true)
    //Или можно написать так: "select * from student order by id desc limit 5",
    //но тогда результат будет получен в порядке убывания
    List<Student> getFiveLastStudents();
}

