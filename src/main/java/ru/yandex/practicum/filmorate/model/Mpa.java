package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mpa {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;

    @Override
    public String toString() {
        return id.toString();
    }
}
