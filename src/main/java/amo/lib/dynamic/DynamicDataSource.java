package amo.lib.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态获取数据源Key
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /*
     * 代码中的determineCurrentLookupKey方法取得一个字符串，
     * 该字符串将与配置文件中的相应字符串进行匹配以定位数据源
     */
    @Override
    protected Object determineCurrentLookupKey() {
        /*
         * DynamicDataSourceContextHolder代码中使用setDataSourceKey
         * 设置当前的数据源Key，在路由类中使用getDataSourceKey进行获取，
         *  交给AbstractRoutingDataSource进行注入使用。
         */
        return DynamicDataContextHolder.getDataSourceKey();
    }
}