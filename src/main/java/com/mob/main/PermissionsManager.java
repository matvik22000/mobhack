package com.mob.main;

import com.mob.Exceptions.AuthorisationException;
import com.mob.Exceptions.WrongUsernameOrPasswordException;
import com.mob.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Component
public class PermissionsManager {
    @Autowired
    private Database db;

    public void checkSession(HttpSession session) {
        if (session.isNew()) {
            session.invalidate();
            throw new AuthorisationException("session is new");
        }
    }

    public User login (String username, String password) throws SQLException {
        User user = db.getUser(username);
        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new WrongUsernameOrPasswordException("");
        }

    }
}
