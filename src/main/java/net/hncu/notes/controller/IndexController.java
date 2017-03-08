package net.hncu.notes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by doobo@foxmail.com on 2017/3/8.
 */
@Controller
public class IndexController {

    @RequestMapping("/sucess")
    public String toSucess(){
        return "index";
    }

    @RequestMapping("/next")
    public String toNext(){
        return "index";
    }

}
