package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SneakyThrows
    @Test
    void testPostUserFailInvalidEmail() {
        User newUser = User.builder()
                .login("Oligarh_s_elmasha")
                .name("Kirill")
                .email("kirill@")
                .birthday(LocalDate.of(1991, 03,22))
                .build();
        String newUserString = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post("/users").contentType("application/json").content(newUserString))
                .andDo(h -> System.out.println(h.getResponse().getStatus()))
                .andExpect(status().is5xxServerError());
    }

    @SneakyThrows
    @Test
    void testPostUserOK() {
        User newUser = User.builder()
                .login("Oligarh_s_elmasha")
                .name("Kirill")
                .email("kirill-bulanov@narod.ru")
                .birthday(LocalDate.of(1991, 03,22))
                .build();
        String newUserString = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(get("/users").contentType("application/json").content(newUserString))
                .andDo(h -> System.out.println(h.getResponse().getStatus()))
                .andExpect(status().is2xxSuccessful());
    }
    @SneakyThrows
    @Test
    void testPostUserInvalidLogin() {
        User newUser = User.builder()
                .login("")
                .name("Kirill")
                .email("kirill-bulanov@narod.ru")
                .birthday(LocalDate.of(1991, 03,22))
                .build();
        String newUserString = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post("/users").contentType("application/json").content(newUserString))
                .andDo(h -> System.out.println(h.getResponse().getStatus()))
                .andExpect(status().is5xxServerError());
    }
    @SneakyThrows
    @Test
    void testPostUserInvalidDOB() {
        User newUser = User.builder()
                .login("Kirill")
                .name("Kirill")
                .email("kirill-bulanov@narod.ru")
                .birthday(LocalDate.of(2991, 03,22))
                .build();
        String newUserString = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post("/users").contentType("application/json").content(newUserString))
                .andDo(h -> System.out.println(h.getResponse().getStatus()))
                .andExpect(status().is5xxServerError());
    }

    @SneakyThrows
    @Test
    void testPostFilmOk() {
        Film newFilm = Film.builder()
                .name("Крестный Отец")
                .description("Фильм про мафию")
                .duration(180)
                .releaseDate(LocalDate.of(1985, 11, 11)).build();
        String newFilmString = objectMapper.writeValueAsString(newFilm);
        mockMvc.perform(post("/films").contentType("application/json").content(newFilmString))
                .andDo(h -> System.out.println(h.getResponse().getStatus()))
                .andExpect(status().is2xxSuccessful());
    }

    @SneakyThrows
    @Test
    void testPostFilmInvalidDOB() {
        Film newFilm = Film.builder()
                .name("Крестный Отец")
                .description("Фильм про мафию")
                .duration(180)
                .releaseDate(LocalDate.of(985, 11, 11)).build();
        String newFilmString = objectMapper.writeValueAsString(newFilm);
        mockMvc.perform(post("/films").contentType("application/json").content(newFilmString))
                .andDo(h -> System.out.println(h.getResponse().getStatus()))
                .andExpect(status().is4xxClientError());
    }

    @SneakyThrows
    @Test
    void testPostFilmInvalidDescription() {
        Film newFilm = Film.builder()
                .name("Крестный Отец")
                .description("Фильм про мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию" +
                        ", мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию" +
                        ", мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию" +
                        ", мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию, мафию")
                .duration(180)
                .releaseDate(LocalDate.of(1985, 11, 11)).build();
        String newFilmString = objectMapper.writeValueAsString(newFilm);
        mockMvc.perform(post("/films").contentType("application/json").content(newFilmString))
                .andDo(h -> System.out.println(h.getResponse().getStatus()))
                .andExpect(status().is5xxServerError());
    }

    @SneakyThrows
    @Test
    void testPostFilmBadDuration() {
        Film newFilm = Film.builder()
                .name("Крестный Отец")
                .description("Фильм про мафию")
                .duration(0)
                .releaseDate(LocalDate.of(1985, 11, 11)).build();
        String newFilmString = objectMapper.writeValueAsString(newFilm);
        mockMvc.perform(post("/films").contentType("application/json").content(newFilmString))
                .andDo(h -> System.out.println(h.getResponse().getStatus()))
                .andExpect(status().is5xxServerError());
    }
}