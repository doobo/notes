package net.hncu.notes.controller;

import net.hncu.notes.services.NotesTransaction;
import net.hncu.notes.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by doobo@foxmail.com on 2017/3/8.
 */
@Controller
public class IndexController {


    @Autowired
    NotesTransaction notesTransaction;

    @RequestMapping("/success")
    public String toSucess() throws Exception{
        return "index";
    }


    @RequestMapping("/next")
    public String toNext() throws Exception{
        User user = new User();
        user.setUsername("doobo");
        System.out.println(user.getUsername());
        notesTransaction.addDataToSQL(user);
        return "index";
    }
}
