package demo;

import net.hncu.notes.services.NotesTransaction;
import net.hncu.notes.services.UserTransaction;
import net.hncu.notes.services.impl.AbstractServices;
import net.hncu.notes.table.User;
import net.hncu.notes.utils.AbstractNotesUtils;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by doobo@foxmail.com on 2017/3/27.
 */
public class HibernateUnit {

    private ApplicationContext ctx = null;

    private NotesTransaction nt = null;

    private UserTransaction ut = null;
    {
        ctx = new ClassPathXmlApplicationContext("beans.xml");
        nt = ctx.getBean(NotesTransaction.class);
        ut = ctx.getBean(UserTransaction.class);
    }

    @Test
    public void testHibernateCriteria(){

        System.out.println(ut.getRootId());
        System.out.println(ut.getSystemStatus());
    }

    @Test
    public void test(){
        //String pwd = AbstractNotesUtils.getMD5("root");

//        System.out.println( ut.updatePWD("root",1,2));
        System.out.println(AbstractNotesUtils.getMD5(
                AbstractNotesUtils.getMD5("root","")
                ,UserTransaction.ROOT_KEY
                ));
    }
}
