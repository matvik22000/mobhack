package com.mob.main;


import com.mob.Exceptions.*;
import com.mob.db.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.SQLException;

@CrossOrigin(allowCredentials = "true")
@Controller
public class HttpController {
    private static final Log log = LogFactory.getLog(HttpController.class);
    @Autowired
    Database db;

    @Autowired
    private PermissionsManager permissionsManager;

    @ResponseBody
    @RequestMapping(path = "/test", produces = "application/json", method = RequestMethod.GET)
    String test() {
        return "\"{success\"}: \"true\"";
    }

    @ResponseBody
    @RequestMapping(path = "login", produces = "application/json", method = RequestMethod.GET)
    Community[] login(@RequestParam String id,
                      @RequestParam String password,
                      final HttpServletRequest request) throws SQLException {

        HttpSession session = request.getSession();
        User user;
        if (!session.isNew()) session.invalidate();
        session = request.getSession();
        try {
            user = permissionsManager.login(id, password);
            session.setAttribute("user", user);
        } catch (WrongUsernameOrPasswordException e) {
            session.invalidate();
            throw e;
        }
        return db.getUserCommunities(user.getId());
    }

    @ResponseBody
    @RequestMapping(path = "reg", produces = "application/json", method = RequestMethod.POST)
    String reg(@RequestParam String id,
               @RequestParam String username,
               @RequestParam String password) throws SQLException {
        db.createUser(id, username, password);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "createProposal", produces = "application/json", method = RequestMethod.POST)
    String createProposal(@RequestParam int community,
                          @RequestParam String title,
                          @RequestParam String description,
                          @RequestParam long date,
                          @RequestParam String author,
                          HttpSession session) throws SQLException {
        permissionsManager.checkSession(session);
        db.createProposal(community, title, description, new Date(date), author);
        return "success";
    }

    @ResponseBody
    @RequestMapping(path = "getProposals", produces = "application/json", method = RequestMethod.GET)
    Proposal[] getProposals(@RequestParam int community,
                            HttpSession session) throws SQLException {
        permissionsManager.checkSession(session);
        User user = (User) session.getAttribute("user");
        Proposal[] proposals = db.getProposals(community);
        for (int i = 0; i < proposals.length; i++) {
            proposals[i] = proposals[i].convertToApiFormat(db, user.getId());
        }
        return proposals;
    }

    @ResponseBody
    @RequestMapping(path = "getComments", produces = "application/json", method = RequestMethod.GET)
    Comment[] getComments(@RequestParam int proposalId,
                          HttpSession session) throws SQLException {
        permissionsManager.checkSession(session);
        Comment[] comments = db.getComments(db.getProposal(proposalId));
        for (int i = 0; i < comments.length; i++) {
            comments[i] = comments[i].convertToApiFormat(db);
        }
        return comments;
    }

    @RequestMapping(value = "/doc", method = RequestMethod.GET)
    void doc(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "/doc/index.html");
        httpServletResponse.setStatus(302);
    }

    @ResponseBody
    @RequestMapping(path = "addComment", produces = "application/json", method = RequestMethod.POST)
    String addComment(@RequestParam String text,
                      @RequestParam String author,
                      @RequestParam int proposal,
                      HttpSession session) throws SQLException {
        permissionsManager.checkSession(session);
        db.addComment(text, author, proposal);
        return "success";

    }

    @ResponseBody
    @RequestMapping(path = "like", produces = "application/json", method = RequestMethod.POST)
    String like(
            @RequestParam int proposal,
            HttpSession session) throws SQLException {
        permissionsManager.checkSession(session);
        User user = (User) session.getAttribute("user");
        db.like(user.getId(), proposal);
        return "success";
    }


    @ResponseBody
    @RequestMapping(path = "dislike", produces = "application/json", method = RequestMethod.POST)
    String dislike(
            @RequestParam int proposal,
            HttpSession session) throws SQLException {
        permissionsManager.checkSession(session);
        User user = (User) session.getAttribute("user");
        db.dislike(user.getId(), proposal);
        return "success";

    }


}
