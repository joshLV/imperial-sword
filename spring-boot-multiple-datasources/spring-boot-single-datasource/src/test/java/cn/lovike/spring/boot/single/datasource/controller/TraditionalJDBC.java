package cn.lovike.spring.boot.single.datasource.controller;

import cn.lovike.spring.boot.single.datasource.entity.DictionaryEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 传统的 JDBC 编程
 *
 * @author lovike
 * @since 2021/4/15
 */
@Slf4j
public class TraditionalJDBC {
    @Test
    public void test() {
        Connection        con    = null;
        PreparedStatement ps     = null;
        ResultSet         result = null;
        try {
            // 1.载入 JDBC 驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            String url      = "jdbc:mysql://localhost:3306/multiple_datasources?useSSL=false&serverTimezone=GMT%2B8&characterEncoding=UTF-8";
            String username = "root";
            String password = "bifrost";

            // 2.填写基本信息，尝试与数据库建立连接
            con = DriverManager.getConnection(url, username, password);

            // 3.发送 SQL 语句，并得到返回结果
            // Statement statement = connection.createStatement();
            // 使用它更安全的子类，防止SQL注入攻击
            String sql = "SELECT id, dict_value, description, creator, gmt_create FROM bifrost_dictionary WHERE creator = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "admin");

            result = ps.executeQuery();
            // 4.处理返回结果
            List<DictionaryEntity> list = new ArrayList<>();
            while (result.next()) {
                Long   id          = result.getLong("id");
                String dictValue   = result.getString("dict_value");
                String description = result.getString("description");
                String creator     = result.getString("creator");
                Date   gmtCreate   = result.getDate("gmt_create");

                DictionaryEntity dictionaryEntity = new DictionaryEntity();
                dictionaryEntity.setId(id);
                dictionaryEntity.setDictValue(dictValue);
                dictionaryEntity.setDescription(description);
                dictionaryEntity.setCreator(creator);
                dictionaryEntity.setGmtCreate(gmtCreate);
                list.add(dictionaryEntity);
            }
            for (DictionaryEntity dictionaryEntity : list) {
                log.info(dictionaryEntity.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5.关闭一些资源
            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
