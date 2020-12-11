package com.mob.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Proposal {
    int id;
    int community;
    String title;
    String description;
    Date date;
    String authorId;
    int relevance;
    boolean userLiked;
    int comments;
    User author;

    public Proposal(int id, int community, String title, String description, Date date, String author) {
        this.id = id;
        this.community = community;
        this.title = title;
        this.description = description;
        this.date = date;
        this.authorId = author;
    }

    public Proposal(int community, String title, String description, Date date, String authorId) {
        this.community = community;
        this.title = title;
        this.description = description;
        this.date = date;
        this.authorId = authorId;
    }

    public Proposal convertToApiFormat(Database db) throws SQLException {
        relevance = db.getRelevance(id);
        comments = db.countComments(id);
        author = db.getUser(authorId);
        userLiked = db.likedByUser(authorId, id);
        return this;
    }
    public Proposal(ResultSet cursor) throws SQLException {
        id = cursor.getInt("id");
        community = cursor.getInt("id");
        title = cursor.getString("title");
        description = cursor.getString("description");
        date = cursor.getTimestamp("date");
        authorId = cursor.getString("author");
    }

    public void putIntoDb(Connection conn) throws SQLException {
        Object[] args = {community, title, description, date, authorId};
        PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("INSERT INTO proposals (community, title, description, date, author) VALUES (?, ?, ?, ?, ?)"), args);
        statement.executeUpdate();

    }

    public User getAuthorName() {
        return author;
    }

    public void setAuthorName(User authorName) {
        this.author = authorName;
    }

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public boolean isUserLiked() {
        return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
