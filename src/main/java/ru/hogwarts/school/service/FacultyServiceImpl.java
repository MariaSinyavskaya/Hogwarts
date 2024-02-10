package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(Long id) {
        logger.info("Was invoked method for find faculty by id");
        return facultyRepository.findById(id).get();
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    @Override
    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty by id");
        logger.warn("Faculty with id = {} was deleted", id);
        facultyRepository.deleteById(id);
    }

    @Override
    public Collection<Faculty> findByColor(String color) {
        logger.info("Was invoked method for find faculties by color");
        logger.debug("Requesting a collection of faculties with {} color", color);
        return facultyRepository.findByColor(color);
    }

    @Override
    public Collection<Faculty> findByNameOrColor(String name, String color) {
        logger.info("Was invoked method for find faculties by name or color");
        logger.debug("Requesting a collection of faculties with name {} or {} color", name, color);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    @Override
    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for find all faculties");
        return facultyRepository.findAll();
    }

    @Override
    public Collection<Student> findStudentsOfFaculty(Long id) {
        logger.info("Was invoked method for find students of faculty by id");
        logger.debug("Requesting the students of faculty with id = {}", id);
        return facultyRepository.getReferenceById(id).getStudents();
    }
}
