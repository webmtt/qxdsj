package org.cimiss2.dwp.util;/**
 * Created by zzj on 2019/7/8.
 */

import com.datastax.driver.core.*;


import java.util.*;

/**
 * @program: cimiss2-dwp
 * @description:
 * @author: zzj
 * @create: 2019-07-08 22:22
 **/
public class CassandraSqlUtil {
    private static Session session = CassandraUtil.getSession();

    public static List<Map<String,Object>> selectByCql(String cql, List<Object> list){
        List<Map<String,Object>> mapList=new ArrayList<>();
        PreparedStatement prepareStatement = session.prepare(cql);
        Object[] objects=new Object[list.size()];
        for(int i=0;i<list.size();i++){
            objects[i]=list.get(i);
        }
        BoundStatement bindStatement = new BoundStatement(prepareStatement).bind(objects);
        ResultSet rs = session.execute(bindStatement);
        Iterator<Row> rsIterator = rs.iterator();
        if (rsIterator.hasNext())
        {
            Row row = rsIterator.next();
            Map<String,Object> map=new HashMap<>();
            ColumnDefinitions columnDefinitions=row.getColumnDefinitions();
            for(int i=0;i<columnDefinitions.size();i++){
                map.put(row.getColumnDefinitions().getName(i),row.getObject(i));
            }
            mapList.add(map);

        }
        return mapList;
    }

    public static void saveByCql(String cql, List<Object> list){
        List<Map<String,Object>> mapList=new ArrayList<>();
        PreparedStatement prepareStatement = session.prepare(cql);
        Object[] objects=new Object[list.size()];
        for(int i=0;i<list.size();i++){
            objects[i]=list.get(i);
        }
        BoundStatement bindStatement = new BoundStatement(prepareStatement).bind(objects);
        session.execute(bindStatement);
    }

}

