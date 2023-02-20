package ru.yandex.practicum.filmorate.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.model.ExceptionMessageEnum.BAD_USER;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUsers(User user) {
        String sql = "INSERT INTO Users " +
                "(Name, " +
                "Login, " +
                "Email, " +
                "Birthday) values " +
                "(?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"User_id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        int userId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return getUserById(userId);
    }

    @Override
    public User updateUser(User user) {
        try {
            String sql = "UPDATE USERS  SET  Name = ?, Login = ?, Email = ?, Birthday = ? WHERE User_id = ?; ";
            jdbcTemplate.update(sql,
                    user.getName(),
                    user.getLogin(),
                    user.getEmail(),
                    java.sql.Date.valueOf(user.getBirthday()),
                    user.getId());
            return getUserById(user.getId());
        } catch (Exception exception) {
            throw new MissingException(BAD_USER.getException());
        }
    }

    @Override
    public void deliteUserById(int userId) {
        try {
            String sql = "delete from USERS where User_id = ?";
            jdbcTemplate.update(sql, userId);
        } catch (Exception exception) {
            throw new MissingException(BAD_USER.getException());
        }
    }

    @Override
    public User getUserById(int userId) {
        String sql = "SELECT u.User_id, u.Name, u.Login, u.Email, u.Birthday " +
                "FROM USERS u " +
                "WHERE User_id = ?";
        Optional<User> user = jdbcTemplate.query(sql, (rs, rowNum) -> makeUsers(rs), userId)
                .stream()
                .findFirst();
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new MissingException(BAD_USER.getException());
        }
    }

    @Override
    public List<User> getUsers() {
        String sql = "select u.User_id, u.Name, u.Login, u.Email, u.Birthday  " +
                "from Users AS u ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUsers(rs));
    }

    @Override
    public List<Integer> getUsersIds() {
        List<Integer> ids = new ArrayList<>();
        getUsers().forEach(user -> ids.add(user.getId()));
        return ids;
    }

    private User makeUsers(ResultSet rs) throws SQLException {
        int id = rs.getInt("User_id");
        String name = rs.getString("Name");
        String login = rs.getString("Login");
        String email = rs.getString("Email");
        LocalDate birthday = rs.getDate("Birthday").toLocalDate();
        Set<Integer> usersFriends = makeUsersFriends(id);
        return User.builder()
                .id(id)
                .name(name)
                .login(login)
                .email(email)
                .birthday(birthday)
                .friends(usersFriends)
                .build();
    }

    public Set<Integer> makeUsersFriends(int id) {
        Set<Integer> usersFriends = new HashSet<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT USER_FRIEND_ID FROM FRENDS_LINE WHERE USER_ID = ?", id);
        while (userRows.next()) {
            usersFriends.add(userRows.getInt("USER_FRIEND_ID"));
        }
        return usersFriends;
    }

    @Override
    public User updateUsersFriend(int userId, int usersFriendId) {
        try {
            String sql = "INSERT INTO FRENDS_LINE (User_id, user_friend_id) values " +
                    "(?,?)";
            jdbcTemplate.update(sql,
                    userId,
                    usersFriendId);
            return getUserById(userId);
        } catch (Exception exception) {
            throw new MissingException(BAD_USER.getException());
        }
    }

    public User deleteUsersFriend(int userId, int usersFriendId) {
        try {
            String sql = "delete from FRENDS_LINE where User_id=? AND User_friend_id=?";
            jdbcTemplate.update(sql,
                    userId,
                    usersFriendId);
            return getUserById(userId);
        } catch (Exception exception) {
            throw new MissingException(BAD_USER.getException());
        }
    }
}
