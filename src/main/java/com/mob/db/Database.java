package com.mob.db;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Component
public class Database {
    @Autowired
    private DataSource dataSource;

    public Proposal[] getProposals(int parentCommunity) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {parentCommunity};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select  * from proposals where community = ?"), args);
            ResultSet cursor = statement.executeQuery();
            List<Proposal> proposals = new ArrayList<>();
            while (cursor.next()) {
                proposals.add(new Proposal(cursor));
            }
            return (Proposal[]) proposals.toArray();
        }
    }


    public int getRelevance(int proposalId) throws SQLException {
        Object[] args = {proposalId};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select count(*) from liked2users where proposal = ?"), args);
            ResultSet cursor = statement.executeQuery();
            cursor.first();
            int likes = cursor.getInt(0);

            statement = SQLUtil.prepareStatement(conn.prepareStatement("select count(*) from disliked2users where proposal = ?"), args);
            cursor = statement.executeQuery();
            cursor.first();
            int dislikes = cursor.getInt(0);

            return likes - dislikes;

        }
    }

    public int countComments(int proposalId) throws SQLException {
        Object[] args = {proposalId};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select count(*) from comments where proposal = ?"), args);
            ResultSet cursor = statement.executeQuery();
            cursor.first();
            return cursor.getInt(0);
        }
    }

    public User getUser(String id) throws SQLException {
        Object[] args = {id};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select * from users where id = ?"), args);
            ResultSet cursor = statement.executeQuery();
            cursor.first();
            String username = cursor.getString("username");
            String password = cursor.getString("password");
            return new User(id, username, password);

        }
    }

    public void createUser(String id, String username, String password) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            User user = new User(id, username, password);
            user.putIntoDb(conn);
        }
    }

    public void createProposal(int community, String title, String description, Date date, String author) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Proposal proposal = new Proposal(community, title, description, date, author);
            proposal.putIntoDb(conn);
        }
    }

    public boolean likedByUser(String userId, int proposal) throws SQLException {
        Object[] args = {userId, proposal};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select count(*) from liked2users where \"user\" = ? and proposal = ?;"), args);
            ResultSet cursor = statement.executeQuery();
            cursor.first();
            int l = cursor.getInt(0);
            return l > 0;
        }
    }

    public Comment[] getComments(Proposal parentProposal) throws SQLException {
        Object[] args = {parentProposal.id};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select * from comments where proposal = ?"), args);
            ResultSet cursor = statement.executeQuery();
            List<Comment> comments = new ArrayList<>();
            while (cursor.next()) {
                int id = cursor.getInt("id");
                String text = cursor.getString("text");
                String author = cursor.getString("author");
                int proposal = cursor.getInt("proposal");
                Comment comment = new Comment(id, text, author, proposal, parentProposal);
                comments.add(comment);
            }
            return (Comment[]) comments.toArray();
        }
    }

    public Proposal getProposal(int proposalId) throws SQLException {
        Object[] args = {};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement(""), args);
            ResultSet cursor = statement.executeQuery();
            cursor.first();
            return new Proposal(cursor);

        }
    }


    public Community[] getCommunities() throws SQLException {
        Object[] args = {};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select * from communities;"), args);
            ResultSet cursor = statement.executeQuery();
            List<Community> communities = new ArrayList<>();
            while (cursor.next()) {
                communities.add(new Community(cursor));
            }
            return (Community[]) communities.toArray();
        }
    }

}