package demo;

import net.hncu.notes.lucene.util.AbstractLuceneIndex;
import org.junit.Test;

import java.io.*;
import java.util.Properties;

/**
 * Created by doobo@foxmail.com on 2017/3/21.
 */
public class SystemDirectory {

    @Test
    public void testUtil() throws Exception{
//        System.out.println(AbstractLuceneIndex.getIndexPath());
        String path = this.getClass().getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        path = java.net.URLDecoder.decode(path, "UTF-8");

        Properties p = new Properties();
        p.setProperty("id","dean");
        p.setProperty("password","123456");
        System.out.println(path+"IKAnalyzer.cfg.xml");
        try{
            PrintStream fW = new PrintStream(new File(path+"IKAnalyzer.cfg.xml"));
            p.storeToXML(fW,"test");
            fW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testProperties(){
        Properties prop = new Properties();
        try {
            String path = this.getClass().getProtectionDomain()
                    .getCodeSource().getLocation().getPath();
            File directory = new File(path+"../");
            System.out.println(directory.getCanonicalPath());

            path = java.net.URLDecoder.decode(path, "UTF-8");
            FileInputStream fis =
                    new FileInputStream(path+"IKAnalyzer.cfg.xml");
            prop.loadFromXML(fis);
            //prop.list(System.out);
            prop.getProperty("path");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDirectory() throws Exception{
        //系统默认临时目录
        System.out.println(System.getProperty("java.io.tmpdir"));

        File directory = new File("..");
        System.out.println(directory.getCanonicalPath()); //G:\code\notes
        System.out.println(directory.getAbsolutePath()); //G:\code\notes\lucene\..





        //当前类的绝对路径
        System.out.println("当前类的绝对路径");
        System.out.println(SystemDirectory.class.getResource(""));
        System.out.println();
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
        System.out.println(ClassLoader.getSystemResource(""));
        System.out.println(Test.class.getClassLoader().getResource(""));
        System.out.println( new File("").getAbsolutePath());

    }
}
