package tkmybatis;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


public abstract class IServiceImpl<T> implements IService<T> {

    private Class<T> entityClass;

    @Autowired
    protected IMapper<T> mapper;

    public IServiceImpl() {
        Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.entityClass = (Class) params[0];
    }

    public IMapper<T> getMapper() {
        return this.mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public T selectByPrimaryKey(Object key) {
        return this.mapper.selectByPrimaryKey(key);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> selectEntity(T entity) {
        return this.mapper.select(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> selectByExample(Object example) {
        return this.mapper.selectByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> selectAll() {
        return this.mapper.selectAll();
    }

    @Override
    @Transactional(readOnly = true)
    public int selectCountByExample(Object example) {
        return this.mapper.selectCountByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public int selectCount(T entity) {
        return this.mapper.selectCount(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public T selectOne(T entity) {
        return this.mapper.selectOne(entity);
    }

    @Override
    @Transactional
    public int save(T entity) {
        return this.mapper.insert(entity);
    }

    @Override
    @Transactional
    public int saveNotNull(T entity) {
        return this.mapper.insertSelective(entity);
    }

    @Override
    @Transactional
    public int saveList(List<T> entityList) {
        return this.mapper.insertList(entityList);
    }

    @Override
    @Transactional
    public int delete(T entity) {
        return this.mapper.delete(entity);
    }

    @Override
    @Transactional
    public int deleteByPrimaryKey(Object key) {
        return this.mapper.deleteByPrimaryKey(key);
    }

    @Override
    @Transactional
    public int deleteByExample(Object example) {
        return this.mapper.deleteByExample(example);
    }

    @Override
    @Transactional
    public int updateByPrimaryKey(T entity) {
        return this.mapper.updateByPrimaryKey(entity);
    }

    @Override
    @Transactional
    public int updateNotNull(T entity) {
        return this.mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional
    public int updateByExample(T entity, Object example) {
        return this.mapper.updateByExample(entity, example);
    }

    @Override
    @Transactional
    public int updateByExampleNotNull(T entity, Object example) {
        return this.mapper.updateByExampleSelective(entity, example);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> selectPageByObject(Object obj, int start, int pageSize, String orderBy) {
        Example example = new Example(this.entityClass);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(obj);
        return this.selectPageByExample(example, start, pageSize, orderBy);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> selectPageByExample(Object example, int start, int pageSize, String orderBy) {
        PageHelper.startPage(start, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            PageHelper.orderBy(orderBy);
        }
        PageInfo<T> pageInfo = new PageInfo<>(this.mapper.selectByExample(example));
        return new Page<>(
                pageInfo.getTotal(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getPages(),
                pageInfo.getSize(),
                pageInfo.getList());
    }
}
