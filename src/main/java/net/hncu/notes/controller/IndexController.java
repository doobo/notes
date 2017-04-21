package net.hncu.notes.controller;
import net.hncu.notes.lucene.NotesLucene;
import net.hncu.notes.services.NotesTransaction;
import net.hncu.notes.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by doobo@foxmail.com on 2017/3/8.
 */
@Controller
public class IndexController {

    @Autowired
    NotesLucene nl;

    @ResponseBody
    @RequestMapping("getLuceneNotes")
    public Object getLuceneNotes(Integer curPage,Integer pageSize){
        if(curPage == null || pageSize == null){
            curPage =1;pageSize=3;
        }
        return nl.getLuceneNoteByTime(curPage,pageSize);
    }


    @ResponseBody
    @RequestMapping("getSimpleNotes")
    public Object getNotesByLucene(Integer curPage,Integer pageSize){
        if(curPage == null || pageSize == null){
            curPage =1;pageSize=3;
        }
        return nl.getLuceneNote(curPage,pageSize);
    }

    @RequestMapping("/success")
    public String toSucess(){
        return "index";
    }


    @RequestMapping("/next")
    public String toNext() throws Exception{
        return "index";
    }
}
