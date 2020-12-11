package com.mob.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Comment {
    int id;
    String text;
    String authorId;
    User author;
    int proposalId;
    Proposal proposal;

    public Comment(String text, String authorId, int proposalId) {
        this.text = text;
        this.authorId = authorId;
        this.proposalId = proposalId;
    }

    public Comment(int id, String text, String authorId, int proposalId, Proposal proposal) {
        this.id = id;
        this.text = text;
        this.authorId = authorId;
        this.proposalId = proposalId;
        this.proposal = proposal;
    }

    public Comment(int id, String text, String authorId, User author, int proposalId, Proposal proposal) {
        this.id = id;
        this.text = text;
        this.authorId = authorId;
        this.author = author;
        this.proposalId = proposalId;
        this.proposal = proposal;
    }

    public Comment convertToApiFormat(Database db) throws SQLException {
        if (author == null) {
            getAuthorFromDb(db);
        }
        if (proposal == null) {
            getProposalFromDb(db);
        }

        return this;
    }

    public void putIntoDb(Connection conn) throws SQLException {
        Object[] args = {text, authorId, proposal};
        PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("insert into comments (text, author, proposal) values (?, ?, ?);"), args);
        statement.executeUpdate();

    }

    public void getAuthorFromDb(Database db) throws SQLException {
        author = db.getUser(authorId);
    }

    public void getProposalFromDb(Database db) throws SQLException {
        proposal = db.getProposal(proposalId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getProposalId() {
        return proposalId;
    }

    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }
}
