package com.mob.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    String  id;
    String username;
    @JsonIgnore
    String password;

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public void putIntoDb(Connection conn) throws SQLException {
        Object[] args = {id, username, password};
        System.out.println(id);
        System.out.println(username);
        System.out.println(password);
        PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("INSERT INTO users(id, username, password)  values (?, ?, ?)"), args);
        statement.executeUpdate();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
