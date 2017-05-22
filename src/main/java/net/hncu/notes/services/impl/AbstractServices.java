package net.hncu.notes.services.impl;

import net.hncu.notes.dao.impl.AbstractHibernate;
import net.hncu.notes.lucene.NoteDocument;
import net.hncu.notes.table.Notes;
import net.hncu.notes.table.PicInfo;
import net.hncu.notes.utils.AbstractNotesUtils;

import java.util.TreeMap;

/**
 * Created by doobo@foxmail.com on 2017/4/30.
 */
public abstract  class AbstractServices {
    /**
     * 根据字符串信息保存图片到数据库
     * @param images
     * @param notes
     * @param hb
     */
    public static void insertImages(String images, Notes notes
            , AbstractHibernate hb,NoteDocument noteDocument) {
        if(images != null && !images.isEmpty()){
            String[] strings = images.split("~");
            for (String str:strings) {
                String[] temps = str.split(">");
                if(temps.length == 2){
                    //.substring(temps[0].lastIndexOf('/')+1)
                    PicInfo picInfo = null;
                    try {
                        picInfo = new PicInfo(temps[0]
                                ,notes,Integer.parseInt(temps[1]),0);
                    } catch (NumberFormatException e) {
                        picInfo = new PicInfo(temps[0],notes,469,0);
                        System.out.println("使用默认宽度-文章id:"+notes.getId());
                    }
                    String hql = "SELECT count(*) FROM PicInfo p WHERE " +
                            "p.picPath = :src";
                    Object count = hb.getQueryByHQL(hql)
                                    .setString("src",temps[0])
                                    .uniqueResult();
                    if(count.toString().equals("0")){
                        hb.addDataByClass(picInfo);
                        if(picInfo.getWidth()>300){
                            noteDocument.setIsPic(1);
                            noteDocument.setPic(picInfo.getPicPath());
                        }
                    }else {
                        System.out.println("图片已存在-文章id:"+notes.getId());
                    }
                }
            }
        }
    }


    /**
     * 设置必要的查询条件
     * @param check
     * @param share
     * @param wd
     */
    public static StringBuffer setHQLByParams(Integer check
            ,Integer share,String wd,Long startTime,Long endTime){
        StringBuffer terms = new StringBuffer();
        if(check != null)
            terms.append(" AND n.check="+check);
        if(share != null)
            terms.append(" AND n.share="+share);
        if(wd != null){
            wd = wd.replaceAll("%", "/%");
            wd = wd.replaceAll("_", "/_");
            wd = "%"+wd+"%";
            terms.append(" AND n.title like :wd ");
        }
        if(startTime != null && endTime != null){
            terms.append(" AND n.firstTime >=:startTime AND n.firstTime <=:endTime");
        }
        terms.append(" ");
        return terms;
    }

    /**
     * 设置模糊查询关键词，替换特殊符合
     * @param wd
     * @return
     */
    public static String getLikeWd(String wd){
        if(wd == null || wd.isEmpty()) return null;
        wd = wd.replaceAll("%", "/%");
        wd = wd.replaceAll("_", "/_");
        wd = "%"+wd+"%";
        return wd;
    }

    public static void setReturnMap(Integer curPage, Integer pageSize,
                              TreeMap<String,Object> map, Object count){
        map.put("count",count);
        map.put("pageCount", AbstractNotesUtils.getPageCount(pageSize,
                Integer.parseInt(map.get("count").toString())));
        map.put("curPage",curPage);
        map.put("pageSize",pageSize);
    }

}
