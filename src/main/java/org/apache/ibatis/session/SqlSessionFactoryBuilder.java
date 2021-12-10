package org.apache.ibatis.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

/**
 * @Description: SqlSessionFactoryBuilder
 * @Author zdp
 * @Date 2021-12-09 13:48
 */
public class SqlSessionFactoryBuilder {

  public SqlSessionFactory build(Reader reader) {
    return build(reader, null, null);
  }

  public SqlSessionFactory build(Reader reader, String environment) {
    return build(reader, environment, null);
  }

  public SqlSessionFactory build(Reader reader, Properties properties) {
    return build(reader, null, properties);
  }

  public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
    try {
      XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        reader.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }

  /**
   * @Description: 读取核心配置文件构建SqlSessionFactory
   * @param: MyBatis-config.xml核心配置文件的InputStream
   * @Author zdp
   * @Date 2021-12-09 13:50
   */
  public SqlSessionFactory build(InputStream inputStream) {
    return build(inputStream, null, null);
  }

  public SqlSessionFactory build(InputStream inputStream, String environment) {
    return build(inputStream, environment, null);
  }

  public SqlSessionFactory build(InputStream inputStream, Properties properties) {
    return build(inputStream, null, properties);
  }

  /**
   * @Description: 以上的重载方法最终都调用的是该方法，可参考该写法，兼容多参数
   * @Author zdp
   * @Date 2021-12-09 13:53
   */
  public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
    try {
      //XMLConfigBuilder 初始化的过程会去初始化BaseBuilder中的Configuration对象，初始化MyBatis中一些默认的配置信息，例如默认的int、long之类的别名配置，默认的TypeHandler类转换器
      XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
      //Configuration config = parse.parse();  通过XPath，解析得到相应属性值，设置到Configuration中
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        inputStream.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }

  /**
   * @Description: 根据装载完成的Configuration对象，初始化 SqlSessionFactory 对象
   * @Author zdp
   * @Date 2021-12-09 17:20
   */
  public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
  }

}
