package tkmybatis;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;


@org.apache.ibatis.annotations.Mapper
public interface IMapper<T> extends Mapper<T>, InsertListMapper<T> {


}
