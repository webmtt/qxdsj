package com.thinkgem.jeesite.mybatis.modules.filedecode.decode;

import com.thinkgem.jeesite.common.utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 建表语句（实体类字段保存在文件中，格式：private	String	D_RECORD_ID;//记录标识）
 * @author yang.kq
 * @version 1.0
 * @date 2020/4/24 15:30
 */
public class TestCreateTableSql {
    public static Map<String, String> javaProperty2SqlColumnMap = new HashMap<>();
    public static Map<String, String> javaSqlColumnMap = new HashMap<>();
    //下边是对应的oracle的生成语句，类型都是oracle，如果是mysql还需要改。
    static {
        javaProperty2SqlColumnMap.put("Integer", "NUMBER(10,0)");
        javaProperty2SqlColumnMap.put("Short", "NUMBER(4)");
        javaProperty2SqlColumnMap.put("Long", "NUMBER(18)");
        javaProperty2SqlColumnMap.put("BigDecimal", "NUMBER(22,2)");
        javaProperty2SqlColumnMap.put("Double", "NUMBER(22,2)");
        javaProperty2SqlColumnMap.put("Float", "NUMBER(10,1)");
        javaProperty2SqlColumnMap.put("Boolean", "NUMBER(1)");
        javaProperty2SqlColumnMap.put("Date", "DATE");
        javaProperty2SqlColumnMap.put("String", "VARCHAR(30)");
    }
    static{
        javaSqlColumnMap.put("Integer", "setInt");
        javaSqlColumnMap.put("Short", "setInt");
        javaSqlColumnMap.put("BigDecimal", "setBigDecimal");
        javaSqlColumnMap.put("Float", "setFloat");
        javaSqlColumnMap.put("Date", "setDate");
        javaSqlColumnMap.put("String", "setString");
    }
    private static void createTableSql(String tableName, String prekey, String tableNameAno){
        File file=new File("E://entity.txt");
        try {
            List<String> list = FileUtils.readLines(file,"gbk");

           String sql="create table "+tableName+"(\r\n";

                for(int i=0,length=list.size();i<length;i++) {
                    String line=list.get(i);
                    String[] lines = line.split(";//");
                    String[] fields = lines[0].split(" ");
                    //字段
                    String field = fields[fields.length - 1];
                    //字段类型
                    String fieldType = fields[fields.length - 2];
                    //注释
                    String annotation = lines[1];
                    sql += " " + field + " " + javaProperty2SqlColumnMap.get(fieldType);
                    if (prekey.equals(field)) {
                        sql += "  PRIMARY KEY ";
                    }
                    if (i==length-1){
                        sql += " comment '" + annotation + "'\r\n";
                        sql+=");";
                    }else{
                        sql += " comment '" + annotation + "',\r\n";
                    }

                }
            System.out.println(sql);
            String tableNameSql="comment on table "+tableName+" is '"+tableNameAno+"'";
            System.out.println(tableNameSql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void createSql(String tableName){
        File file=new File("E://entity.txt");
        try {
            List<String> list = FileUtils.readLines(file,"gbk");
            int count=0;
            String sql="insert INTO "+tableName+"( \r\n";
            String vsql="\r\n)values(\r\n";
            for(int i=0,length=list.size();i<length;i++) {
                String line=list.get(i);
                String[] lines = line.split(";//");
                String[] fields = lines[0].split(" ");
                //字段
                String field = fields[fields.length - 1];
                count++;
                if(count>30) {
                    if(i==length-1) {
                        sql += field + "\r\n";
                        vsql += "?";
                    }else{
                        sql += field + ",\r\n";
                        vsql += "?,\r\n";
                    }
                    count=0;
                }else{
                    if(i==length-1) {
                        sql += field ;
                        vsql+="?";
                    }else{
                        sql += field + ",";
                        vsql+="?,";
                    }

                }
            }
            System.out.println(sql+vsql+");");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 创建添加sql赋值语句  格式：   psstatement.setString(ii++, meadowValueTab.getD_RECORD_ID());// 记录标识
     * @param psstatement
     * @param name -实体类对象
     */
    private static void insertTableSetValueSql(String psstatement,String name){
        File file=new File("E://entity.txt");
        try {
            List<String> list = FileUtils.readLines(file,"gbk");
            String sql1="";
            for(int i=0,length=list.size();i<length;i++) {
                String sql="";
                sql+=psstatement+".";
                String line=list.get(i);
                String[] lines = line.split(";//");
                String[] fields = lines[0].split(" ");
                //字段
                String field = fields[fields.length - 1];
                //字段类型
                String fieldType = fields[fields.length - 2];
                //注释
                String annotation = lines[1];
                sql+=javaSqlColumnMap.get(fieldType);
                sql+="(ii++, ";
                sql+=name+".get";
                sql+=field.substring(0, 1).toUpperCase() + field.substring(1);
                sql+="());";
                sql+="//"+annotation;
                sql+="\r\n";
                sql1 += "if(" + name + ".get" + field.substring(0, 1).toUpperCase() + field.substring(1) + "()==null){\r\n";
                sql1 += "   " + psstatement + "." + javaSqlColumnMap.get(fieldType) + "(ii++, 999999);\r\n";
                sql1 += "}else{\r\n   " + sql + "}\r\n";
            }
            System.out.println(sql1);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createParamInsertSql(String parent_id){
        File file=new File("E://insert.txt");
        try {
            List<String> list = FileUtils.readLines(file,"gbk");

            for(int i=0,length=list.size();i<length;i++) {
                String sql="INSERT INTO `neimeng_qxdsj_share_yanfa`.`sup_reportsearchconf`(`id`, `param_name`, `param_code`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES (";
                String line=list.get(i);
                String[] lines = line.split(";//");
                String[] fields = lines[0].split(" ");
                //字段
                String field = fields[fields.length - 1];
                //字段类型
                String fieldType = fields[fields.length - 2];
                //注释
                String annotation = lines[1];
               sql+="'"+UUID.randomUUID()+"','";
               sql+=annotation+"','";
               sql+=field+"','";
                sql+=parent_id+"'";
                sql+=", '1', '2019-12-10 14:35:25', '1', '2019-12-10 14:35:25','', '0');";
                System.out.println(sql);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
//        createTableSql("VIEW_PARAM_SUNLIGHT","D_RECORD_ID","日照值信息存储表");
//        createSql("meadowValueTable");
//        insertTableSetValueSql("psMonth","mvt  psMonth.setString(ii++, 999999);");
//        createParamInsertSql("c05a1d880c3711eabef68cec4b98d79d");
    }
}
