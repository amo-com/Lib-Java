package amo.lib.dynamic;

import amo.lib.dynamic.interfaces.TargetDataBase;
import amo.lib.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.*;

@Aspect
@Order(-1)//保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {
    /*
     * 创建切点
     * within标记表示注解会作用于内部,比如标记class,所有method都可以触发,里面的参数如果不是同级目录需要写完整路径
     * annotation是有这个注解时才触发
     */
    @Pointcut("@within(amo.lib.dynamic.interfaces.TargetDataBase) || @annotation(amo.lib.dynamic.interfaces.TargetDataBase)")
    private void cut() {
    }

    /*
     * @Before：在方法执行之前进行执行：
     * @annotation(targetDataSource)：
     * 会拦截注解targetDataSource的方法，否则不拦截。
     */
    @Before("cut()")
    public void changeDataSource(JoinPoint point) throws Throwable {
        TargetDataBase targetDataBase = this.getAnnotation(point, TargetDataBase.class);
        if (targetDataBase != null) {
            String site = DynamicDataContextHolder.getSite();// 获取线程site
            String siteLower = site == null ? "" : site.toLowerCase();
            String datasource = targetDataBase.DataSource();
            if (datasource != null && !StringUtils.isBlank(datasource)) {
                String dataSourceKey = String.format(datasource, siteLower);
                //使用默认数据源
                DynamicDataContextHolder.setDataSourceKey(dataSourceKey);
            }
        }
    }

    /*
     * @After：在方法执行之后进行执行：
     * @annotation(targetDataSource)：
     * 会拦截注解targetDataSource的方法，否则不拦截。
     */
    @After("cut()")
    public void restoreDataSource() {
        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
        DynamicDataContextHolder.clearDataSourceKey();
    }

    /*
     * 获取注解
     */
    private <A extends Annotation> A getAnnotation(JoinPoint point, Class<A> annotationClass) {
        Objects.requireNonNull(annotationClass);
        A cusAnnotation = point.getTarget().getClass().getAnnotation(annotationClass);// 尝试直接从traget上获取指定注解
        // 没获取到,从接口上获取,因为接口可能存在多个,所以获取到一个就结束
        if (cusAnnotation == null) {
            Class<?>[] customInterfaces = point.getTarget().getClass().getInterfaces();
            if (customInterfaces != null && customInterfaces.length > 0) {
                for (Class<?> customInterface : customInterfaces) {
                    cusAnnotation = customInterface.getAnnotation(annotationClass);
                    if (cusAnnotation != null) {
                        break;
                    }
                }
            }
        }

        return cusAnnotation;
    }
}