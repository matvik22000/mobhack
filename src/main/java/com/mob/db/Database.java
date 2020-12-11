package com.mob.db;


import com.mob.Exceptions.CommunityDoesntExistException;
import com.mob.Exceptions.ProposalDoesntExistException;
import com.mob.Exceptions.RowDoesntExistException;
import com.mob.Exceptions.UserDoesntException;
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
            cursor.next();
            int likes = cursor.getInt(0);

            statement = SQLUtil.prepareStatement(conn.prepareStatement("select count(*) from disliked2users where proposal = ?"), args);
            cursor = statement.executeQuery();
            cursor.next();
            int dislikes = cursor.getInt(0);

            return likes - dislikes;

        }
    }

    public int countComments(int proposalId) throws SQLException {
        Object[] args = {proposalId};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select count(*) from comments where proposal = ?"), args);
            ResultSet cursor = statement.executeQuery();
            cursor.next();
            return cursor.getInt(0);
        }
    }

    public void addComment(String text, String author, int proposal) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            checkProposal(conn, proposal);
            checkUser(conn, author);
            Comment comment = new Comment(text, author, proposal);
            comment.putIntoDb(conn);
        }
    }

    public void checkProposal(Connection conn, int proposalId) throws SQLException {
        Object[] args = {proposalId};
        PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select  count(*) from proposals where id = ?"), args);
        ResultSet cursor = statement.executeQuery();
        cursor.next();
        if(!(cursor.getInt(0) > 0)){
            throw new ProposalDoesntExistException("");
        }
    }

    public void checkUser(Connection conn, String author) throws SQLException {
        Object[] args = {author};
        PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select  count(*) from users where id = ?"), args);
        ResultSet cursor = statement.executeQuery();
        cursor.next();
        if(!(cursor.getInt(0) > 0)){
           throw new UserDoesntException("");
        }
    }

    public void checkCommunity(Connection conn, int communityId) throws SQLException {
        Object[] args = {communityId};
        PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select  count(*) from communities where id = ?"), args);
        ResultSet cursor = statement.executeQuery();
        cursor.next();
        if(!(cursor.getInt(0) > 0)){
            throw new CommunityDoesntExistException("");
        }
    }

    public User getUser(String id) throws SQLException {
        Object[] args = {id};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select * from users where id = ?"), args);
            ResultSet cursor = statement.executeQuery();
            cursor.next();
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
            checkCommunity(conn, community);
            checkUser(conn, author);
            Proposal proposal = new Proposal(community, title, description, date, author);
            proposal.putIntoDb(conn);
        }
    }

    public boolean likedByUser(String userId, int proposal) throws SQLException {
        Object[] args = {userId, proposal};
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select count(*) from liked2users where \"user\" = ? and proposal = ?;"), args);
            ResultSet cursor = statement.executeQuery();
            cursor.next();
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
            cursor.next();
            return new Proposal(cursor);

        }
    }

    public Community[] getUserCommunities(String user) throws SQLException {
        Object[] args = {user};
        try (Connection conn = dataSource.getConnection()) {
            checkUser(conn, user);
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select * from community2users where \"user\" = ?;"), args);
            ResultSet cursor = statement.executeQuery();
            List<Community> communities = new ArrayList<>();
            while(cursor.next()) {
                Object[] args2 = {cursor.getInt("community")};
                PreparedStatement statement2 = SQLUtil.prepareStatement(conn.prepareStatement("select * from community2users where \"user\" = ?;"), args2);
                ResultSet cursor2 = statement2.executeQuery();
                communities.add(new Community(cursor2));
            }
            return (Community[]) communities.toArray();
        }
    }

    public void like(String user, int proposal) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            checkProposal(conn, proposal);
            checkUser(conn, user);
            Object[] args = {user, proposal};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("insert into liked2users(\"user\", proposal) values (?, ?)"), args);
            statement.executeUpdate();
        }
    }

    public void dislike(String user, int proposal) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            checkProposal(conn, proposal);
            checkUser(conn, user);
            Object[] args = {user, proposal};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("insert into disliked2users(\"user\", proposal) values (?, ?)"), args);
            statement.executeUpdate();
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