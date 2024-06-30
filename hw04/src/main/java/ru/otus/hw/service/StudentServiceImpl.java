package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    @Override
    public Student determineCurrentStudent(String userName, String userLastName) {
        return new Student(userName, userLastName);
    }
}
