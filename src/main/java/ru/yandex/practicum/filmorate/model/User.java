package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private int id;
    @Email
    private String email;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private final Set<Integer> notAprovedFriends = new HashSet<>();
    private final Set<Integer> aprovedFriends = new HashSet<>();



    public Set<Integer> getFriends() {
        return notAprovedFriends;
    }

    public void deleteFriends(int userId) {
        notAprovedFriends.remove(userId);
    }

    public void setNotAprovedFriendsById(Integer userId) {
        notAprovedFriends.add(userId);
    }

    public void setAprovedFriendsById(Integer userId) {
        notAprovedFriends.remove(userId);
        aprovedFriends.add(userId);
    }

    public String isAprovedFriend(Integer userId){
        if (aprovedFriends.contains(userId)){
            return "Aproved";
        }
        else return "Not aproved";
    }
}
