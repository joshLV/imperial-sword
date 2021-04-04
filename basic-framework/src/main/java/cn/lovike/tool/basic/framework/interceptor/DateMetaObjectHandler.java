// package cn.lovike.tool.basic.framework.interceptor;
//
// import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
// import org.apache.ibatis.reflection.MetaObject;
// import org.springframework.stereotype.Component;
//
// import java.util.Date;
//
// /**
//  * Mybatis Plus 属性自动填充
//  *
//  * @author lovike
//  * @since 2020-02-16
//  */
// @Component
// public class DateMetaObjectHandler implements MetaObjectHandler {
//
//     @Override
//     public void insertFill(MetaObject metaObject) {
//         // 看实体类中是否有这个属性，有的话就执行。没有就不执行
//         boolean hasSetter = metaObject.hasSetter("gmtCreate");
//         if (hasSetter) {
//             setInsertFieldValByName("gmtCreate", new Date(), metaObject);
//         }
//     }
//
//     @Override
//     public void updateFill(MetaObject metaObject) {
//         // 如果预先自己设置了值，则设置不使用 MP 的自动填充
//         Object val = getFieldValByName("gmtModified", metaObject);
//         if (val == null) {
//             setUpdateFieldValByName("gmtModified", new Date(), metaObject);
//         }
//     }
// }
