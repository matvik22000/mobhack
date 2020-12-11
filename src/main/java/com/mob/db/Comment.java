package com.mob.db;

import java.sql.SQLException;

public class Comment {
    int id;
    String text;
    String authorId;
    User author;
    int proposalId;
    Proposal proposal;

    public Comment(int id, String text, String authorId, int proposalId) {
        this.id = id;
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

    public void getAuthorFromDb(Database db) throws SQLException {
        author = db.getUser(authorId);
    }

    public void getProposalFromDb(Database db) throws SQLException {
        proposal = db.getProposal(proposalId);
    }

}
