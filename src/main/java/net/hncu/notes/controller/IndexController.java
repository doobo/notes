package net.hncu.notes.controller;
import net.hncu.notes.lucene.NotesLucene;
import net.hncu.notes.services.NotesTransaction;
import net.hncu.notes.services.UserTransaction;
import net.hncu.notes.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @RequestMapping("getLuceneNotes")
    public Object getLuceneNotes(Integer curPage,Integer pageSize){
        if(curPage == null || pageSize == null){
            curPage =1;pageSize=3;
        }
        return nl.getRootLuceneNoteByTime(curPage,pageSize,ut.getRootId());
    }


    @ResponseBody
    @RequestMapping("getSimpleNotes")
    public Object getNotesByLucene(Integer curPage,Integer pageSize){
        if(curPage == null || pageSize == null){
            curPage =1;pageSize=3;
        }
        return nl.getLuceneNote(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("getSearchInPicInfo")
    public Object getSearchInPicInfo(Integer curPage,Integer pageSize
            ,String wd){
        return nt.getSearchInPicInfo(curPage,pageSize,wd);
    }

    @RequestMapping("/next")
    public String toNext() throws Exception{
        return "index";
    }

    @ResponseBody
    @RequestMapping("isLogin")
    public Object isLogin(HttpSession session){
        if(session.getAttribute("user") != null){
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("isMaster")
    public Object isMaster(HttpSession session){
        if(session.getAttribute("master") != null){
            return true;
        }
        return false;
    }

    @ResponseBody
    @RequestMapping("isRoot")
    public Object isRoot(HttpSession session){
        if(session.getAttribute("root") != null){
            return true;
        }
        return false;
    }
}
