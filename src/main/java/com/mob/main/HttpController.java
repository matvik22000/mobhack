package com.mob.main;


import com.mob.Exceptions.*;
import com.mob.db.Database;
import com.mob.db.Proposal;
import com.mob.db.User;
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
    private Database db;

    @Autowired
    private PermissionsManager permissionsManager;

    @ResponseBody
    @RequestMapping(path = "/test", produces = "application/json", method = RequestMethod.GET)
    String test() {
        return "\"{success\"}: \"true\"";
    }

    @ResponseBody
    @RequestMapping(path = "getProposals", produces = "application/json", method = RequestMethod.GET)
    Proposal[] getProposals(@RequestParam int community) throws SQLException {
        return db.getProposals(community);
    }

    @ResponseBody
    @RequestMapping(path = "login", produces = "application/json", method = RequestMethod.GET)
    String login(@RequestParam String username,
                 @RequestParam String password,
                 final HttpServletRequest request) throws SQLException {

        HttpSession session = request.getSession();
        if (!session.isNew()) session.invalidate();
        session = request.getSession();
        try {
            User user = permissionsManager.login(username, password);
            session.setAttribute("user", user);
        } catch (WrongUsernameOrPasswordException e) {
            session.invalidate();
            throw e;
        }
        return "success";
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
                          @RequestParam String author) throws SQLException {
        db.createProposal(community, title, description, new Date(date), author);
        return "success";

    }
    
    @RequestMapping(value = "/doc", method = RequestMethod.GET)
    void doc(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "/doc/index.html");
        httpServletResponse.setStatus(302);
    }

}
