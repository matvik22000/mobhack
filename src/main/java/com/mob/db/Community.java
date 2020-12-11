package com.mob.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Community {
    int id;
    String name;
    String description;

    public Community(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Community(ResultSet cursor) throws SQLException {
        id = cursor.getInt("id");
        name = cursor.getString("name");
        description = cursor.getString("description");
    }
}
