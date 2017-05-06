package demo;

import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;

/**
 * Created by Administrator on 2017/3/8.
 */
public class UnitTest {



    private String createBase64Image(String path) {
        BufferedInputStream bis;
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Test
    public void testUnit(){
        HashSet<String > set = new HashSet<>();
        String text = "据说WWDC要推出iPhone6要出了？与iPhone5s相比怎样呢？@2014巴西世界杯";
        StringReader reader = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(reader,true);// 当为true时，分词器进行最大词长切分
        Lexeme lexeme = null;
        try {
            while((lexeme = ik.next())!=null)
                set.add(lexeme.getLexemeText());
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            reader.close();
        }
    }
}
