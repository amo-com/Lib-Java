package amo.lib.dynamic.interfaces;

import java.lang.annotation.*;

/**
 * 标记数据源,配合threadlocal的参数通过AOP实现自动切换数据源
 * 核心code在DynamicDataSourceAspect下changeDataSource
 * 目前支持格式示例:record,%s.part,可以有一个参数位,运行时会被替换为DynamicDataContextHolder中获取的site,替换后格式为record,[site].part
 * 名字要和properties中配置的custom.datasource.names下的名字保持一致,特别是大小写问题,site统一小写
 * 使用时需要在接口,class和method上加注解,标机dao下的方法要查询的datasource名字,接口必须是代理类,否则会因为实现类不继承接口注解造成无法拦截
 * site不一定需要,特殊业务需求,需要同一分code运行多个site的多套数据库,属于二维关系
 *
 *  用于标记Dao的数据库类型,只区分数据库,不区分Site,配合Site可以达到自动匹配DataSource
 *  可以标记Class,interface,也可以标记Method,参考DynamicDataSourceAspect，springboot 2.x.x才可以标记接口,Jpa和Mybatis这种代理生成
 *  实现类的要基于Aop 5.X(springboot 2.x.x)才能通过Aop切入
 *  Jpa,Mybatis都可以使用
 *  每个Dao层的同一个Mapper,Reposity下的应该都是一个数据库的操作,所以直接标记在Class上,维护Dao的人员负责维护Dao和datasource的关系
 *  这样Service层和Control等其他调用者不用管Dao的DataSource
 *  是谁,只需要切换自己需要的Site,Dao下的method在执行时会结合threadlocal中的site和Dao上标记的数据库类型自动切换datasource
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface TargetDataBase {
    String DataSource() default "";
}
