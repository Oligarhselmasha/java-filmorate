package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDAOImpl;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MpaService {
    private final MpaDAOImpl mpaDAO;

    public List<Mpa> getMpa() {
        return mpaDAO.getMpa();
    }

    public Mpa getMpasById(int id) {
        return mpaDAO.getMpaById(id);
    }
}
