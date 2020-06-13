package com._5fu8.admin.controller;
import com._5fu8.admin.lucene.NotesLucene;
import com._5fu8.admin.services.NotesTransaction;
import com._5fu8.admin.services.UserTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


/**
 * Created by doobo@foxmail.com on 2017/3/8.
 */
@Controller
public class IndexController {

    @Autowired
    NotesLucene nl;

    @Autowired
    NotesTransaction nt;

    @Autowired
    UserTransaction ut;

    @ResponseBody
    @PostMapping("getLuceneNotes")
    public Object getLuceneNotes(Integer curPage,Integer pageSize){
        if(curPage == null || pageSize == null){
            curPage =1;pageSize=3;
        }
        return nl.getRootLuceneNoteByTime(curPage,pageSize,ut.getRootId());
    }


    @ResponseBody
    @PostMapping("getSimpleNotes")
    public Object getNotesByLucene(Integer curPage,Integer pageSize){
        if(curPage == null || pageSize == null){
            curPage =1;pageSize=3;
        }
        return nl.getLuceneNote(curPage,pageSize);
    }

    @ResponseBody
    @GetMapping("getSearchInPicInfo")
    public Object getSearchInPicInfo(Integer curPage,Integer pageSize
            ,String wd){
        return nt.getSearchInPicInfo(curPage,pageSize,wd);
    }

    @GetMapping("/next")
    public String toNext() throws Exception{
        return "index";
    }

    @ResponseBody
    @PostMapping("isLogin")
    public Boolean isLogin(HttpSession session){
        if(session.getAttribute("user") != null){
            return true;
        }
        return false;
    }

    @ResponseBody
    @PostMapping("isMaster")
    public Boolean isMaster(HttpSession session){
        if(session.getAttribute("master") != null){
            return true;
        }
        return false;
    }

    @ResponseBody
    @PostMapping("isRoot")
    public Boolean isRoot(HttpSession session){
        if(session.getAttribute("root") != null){
            return true;
        }
        return false;
    }
}
