package demo;

import net.hncu.notes.services.NotesTransaction;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by doobo@foxmail.com on 2017/3/27.
 */
public class HibernateUnit {

    private ApplicationContext ctx = null;

    private NotesTransaction nt = null;
    {
        ctx = new ClassPathXmlApplicationContext("beans.xml");
        nt = ctx.getBean(NotesTransaction.class);
    }

    @Test
    public void testHibernateCriteria(){
          for (Object obj : nt.getSimpleNotes(1,1,null)){
              System.out.println(obj);
          }
    }
}
