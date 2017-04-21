package net.hncu.notes.controller;
import net.hncu.notes.lucene.util.AbstractLuceneIndex;
import net.hncu.notes.services.NotesTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.TreeMap;


/**
 * Created by doobo@foxmail.com on 2017/3/8.
 */
@RequestMapping("/kingmaster/")
@Controller
public class KingMasterController {

    @Autowired
    NotesTransaction nt;

    @RequestMapping("/success")
    public String toSuccess(){
        return "index";
    }

    @ResponseBody
    @RequestMapping("/getSimpleNotes")
    public Object toNext(Integer curPage,Integer pageSize){
        TreeMap<String,Object> map = new TreeMap<>();
        Object data = nt.getSimpleNotes(curPage,pageSize,map);
        map.put("notes",nt.getSimpleNotes(curPage,pageSize,map));
        return map;
    }

    @ResponseBody
    @RequestMapping("getSimpleChildType")
    public Object getSimpleChildType(Integer curPage,Integer pageSize){
        return nt.getSimpleChildType(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("getSimpleUser")
    public Object getSimpleUser(Integer curPage,Integer pageSize){
        return nt.getSimpleUser(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("getSimpleKingMaster")
    public Object getSimpleKingMaster(Integer curPage,Integer pageSize){
        return nt.getSimpleKingMaster(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("getDelUser")
    public Object getDelUsers(Integer curPage,Integer pageSize){
        return nt.getDelUser(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("getDelNotes")
    public Object getDelNotes(Integer curPage,Integer pageSize){
        return nt.getDelNotes(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("getIKText")
    public Object getIKLuceneCi(String text){
        return nt.getIKLuceneCi(text);
    }

    @ResponseBody
    @RequestMapping("clearLuceneCache")
    public Object clearLuceneCache(){
        return AbstractLuceneIndex.clearCache();
    }

    @ResponseBody
    @RequestMapping("getNotesNoCheck")
    public Object getNotesNoCheck(Integer curPage,Integer pageSize){
        return nt.getNotesNoCheck(curPage,pageSize);
    }

    @ResponseBody
    @RequestMapping("changeCheck")
    public Object changeCheck(Integer id,Integer check){
        return nt.changeCheck(id,check);
    }

    @ResponseBody
    @RequestMapping("changeShare")
    public Object changeShare(Integer id,Integer share){
        return nt.changeShare(id,share);
    }

    @ResponseBody
    @RequestMapping("changeStatus")
    public Object changeStatus(Integer id,Integer status){
        return nt.changeStatus(id,status);
    }

}
