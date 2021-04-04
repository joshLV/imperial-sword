package tkmybatis;


import java.util.List;

public interface IService<T> {

    T selectByPrimaryKey(Object key);

    List<T> selectEntity(T entity);

    List<T> selectByExample(Object example);

    List<T> selectAll();

    int selectCountByExample(Object example);

    int selectCount(T entity);

    T selectOne(T entity);

    int save(T entity);

    int saveNotNull(T entity);

    int saveList(List<T> entityList);

    int delete(T entity);

    int deleteByPrimaryKey(Object key);

    int deleteByExample(Object example);

    int updateByPrimaryKey(T entity);

    int updateNotNull(T entity);

    int updateByExample(T entity, Object example);

    int updateByExampleNotNull(T entity, Object example);

    Page<T> selectPageByObject(Object example, int start, int pageSize, String orderBy);

    Page<T> selectPageByExample(Object example, int start, int pageSize, String orderBy);

}
