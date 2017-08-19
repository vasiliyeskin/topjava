package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Vaisiliy Es'kin on 08/17/17.
 */
public class UserUtil {
    public static List<User> USERS = Arrays.asList(
            new User(null, "name", "mail", "password", Role.ROLE_USER),
            new User(null, "name1", "mail1", "password1", Role.ROLE_USER),
            new User(null, "name2", "mail2", "password2", Role.ROLE_USER),
            new User(null, "name3", "mail3", "password3", Role.ROLE_USER),
            new User(null, "name4", "mail4", "password4", Role.ROLE_ADMIN)
    );
}
