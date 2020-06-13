package demo;

import com._5fu8.admin.AdminApplication;
import com._5fu8.admin.services.NotesTransaction;
import com._5fu8.admin.services.UserTransaction;
import com._5fu8.admin.table.Notes;
import com._5fu8.admin.utils.AbstractNotesUtils;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by doobo@foxmail.com on 2017/3/27.
 */
@SpringBootTest(classes = {AdminApplication.class})
public class HibernateUnit {

    @Resource
    private NotesTransaction nt;

    @Resource
    private UserTransaction ut;



    @Test
    public void testHibernateCriteria(){
        System.out.println(ut.getRootId());
        System.out.println(ut.getSystemStatus());
    }

    @Test
    void test(){
        Notes obj = nt.getNoteByID(1);
        System.out.println(JSON.toJSON(obj));
    }

    @Test
    void tesSave(){

    }
}
