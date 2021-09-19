package org.tinygame.herostory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.util.Resources_pt_BR;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 使用mybatis连接
 * @author sunquanhu
 */
public class MysqlSessionFactory {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlSessionFactory.class);


    /**
     * mybatis sql会话工程
     */
    private static SqlSessionFactory _sqlSessionFactory = null;

    /**
     * 私有化默认构造函数
     */
    private MysqlSessionFactory(){};

    /**
     * 初始化数据库连接
     */
   static void init(){

        LOGGER.info("开始初始化mysql");
        try (InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");){
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            _sqlSessionFactory = sqlSessionFactory;
            //测试sql连接
            SqlSession sqlSession = getSqlSession();
            sqlSession.getConnection().createStatement().execute("select 1");
            sqlSession.close();
            LOGGER.info("mysql连接成功");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }

    }

    /**
     * 获取连接
     * @return
     */
   static SqlSession getSqlSession(){
       if (null == _sqlSessionFactory){
           throw new RuntimeException("SqlSessionFactory尚未初始化");
       }
      return  _sqlSessionFactory.openSession(true);
   }
}
