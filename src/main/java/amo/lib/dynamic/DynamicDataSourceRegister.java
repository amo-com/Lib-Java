package amo.lib.dynamic;

import amo.lib.utils.StringUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final String DATASOURCE_NAMES = "custom.datasource.names";
    private static final String DATASOURCE_DEFAULT = "spring.datasource.default";
    private static final String DATASOURCE_DYNAMIC = "spring.datasource.dynamic";
    private static final String DATASOURCE_CUSTOM_PREFIX = "custom.datasource.";

    // 如配置文件中未指定数据源类型，使用该默认值
    private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";
    // 默认数据源
    private DataSource defaultDataSource;
    private Map<String, DataSource> customDataSources = new HashMap<String, DataSource>();
    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases(); // 别名

    static {
        // 由于部分数据源配置不同，所以在此处添加别名，避免切换数据源出现某些参数无法注入的情况
        aliases.addAliases("url", new String[] { "jdbc-url" });
        aliases.addAliases("username", new String[] { "user" });
    }

    private Environment env; // 配置上下文（也可以理解为配置文件的获取工具）
    private Binder binder; // 参数绑定工具
    /*
     * 加载多数据源配置
     */

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        binder = Binder.get(env); // 绑定配置器
    }

    /**
     * ImportBeanDefinitionRegistrar接口的实现方法，通过该方法可以按照自己的方式注册bean
     *
     * @param annotationMetadata
     * @param beanDefinitionRegistry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
            BeanDefinitionRegistry beanDefinitionRegistry) {
        Map dynamicConfig = binder.bind(this.DATASOURCE_DYNAMIC, Map.class).get(); // dynamic 模板
        customDataSources = new HashMap<>(); // 默认配置

        // 默认数据源
        Map defaultProperties, defaultConfig = binder.bind(this.DATASOURCE_DEFAULT, Map.class).get(); // default
        defaultProperties = getProperties(dynamicConfig, defaultConfig);
        String typeStr = (String) defaultProperties.get("type"); // 默认数据源类型
        Class<? extends DataSource> clazz = getDataSourceType(typeStr); // 获取数据源类型
        defaultDataSource = bind(clazz, defaultProperties); // 绑定默认数据源参数

        String dataSourceNames = env.getProperty(this.DATASOURCE_NAMES);
        // 用户自定义数据源
        if (!StringUtils.isBlank(dataSourceNames)) {
            for (String dataSourceName : dataSourceNames.split(",")) {
                Map customProperties,
                        customConfig = binder.bind(this.DATASOURCE_CUSTOM_PREFIX + dataSourceName, Map.class).get();
                customProperties = getProperties(dynamicConfig, customConfig);
                clazz = getDataSourceType((String) customConfig.get("type"));
                DataSource ds = bind(clazz, customConfig); // 绑定参数
                customDataSources.put(dataSourceName, ds); // 获取数据源的key，以便通过该key可以定位到数据源
            }
        }

        GenericBeanDefinition define = new GenericBeanDefinition(); // bean定义类
        define.setBeanClass(DynamicDataSource.class); // 设置bean的类型，此处MultiDataSource是继承AbstractRoutingDataSource的实现类
        MutablePropertyValues mpv = define.getPropertyValues(); // 需要注入的参数，类似spring配置文件中的<property/>
        mpv.add("defaultTargetDataSource", defaultDataSource); // 添加默认数据源，避免key不存在的情况没有数据源可用
        mpv.add("targetDataSources", customDataSources); // 添加其他数据源
        beanDefinitionRegistry.registerBeanDefinition("datasource", define); // 将该bean注册为datasource，不使用springboot自动生成的datasource
    }

    private Map getProperties(Map dynamicConfig, Map config) {
        Map properties;
        if ((boolean) config.getOrDefault("extend", Boolean.TRUE)) { // 获取extend字段，未定义或为true则为继承状态
            properties = new HashMap(dynamicConfig); // 继承默认数据源配置
            properties.putAll(config); // 添加数据源参数
        } else {
            properties = config; // 不继承默认配置
        }
        return properties;
    }

    /**
     * 通过字符串获取数据源class对象
     *
     * @param typeStr
     * @return
     */
    private Class<? extends DataSource> getDataSourceType(String typeStr) {
        Class<? extends DataSource> type;
        try {
            if (!StringUtils.isBlank(typeStr)) { // 字符串不为空则通过反射获取class对象
                type = (Class<? extends DataSource>) Class.forName(typeStr);
            } else {
                type = HikariDataSource.class; // 默认为hikariCP数据源，与springboot默认数据源保持一致
                // type = (Class<? extends DataSource>) Class.forName((String) DATASOURCE_TYPE_DEFAULT);//
            }
            return type;
        } catch (Exception e) {
            throw new IllegalArgumentException("can not resolve class with type: " + typeStr); // 无法通过反射获取class对象的情况则抛出异常，该情况一般是写错了，所以此次抛出一个runtimeexception
        }
    }

    /**
     * 绑定参数，以下三个方法都是参考DataSourceBuilder的bind方法实现的，目的是尽量保证我们自己添加的数据源构造过程与springboot保持一致
     *
     * @param result
     * @param properties
     */
    private void bind(DataSource result, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[] { source.withAliases(aliases) });
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result)); // 将参数绑定到对象
    }

    private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[] { source.withAliases(aliases) });
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get(); // 通过类型绑定参数并获得实例对象
    }

    /**
     * @param clazz
     * @param sourcePath 参数路径，对应配置文件中的值，如: spring.datasource
     * @param <T>
     * @return
     */
    private <T extends DataSource> T bind(Class<T> clazz, String sourcePath) {
        Map properties = binder.bind(sourcePath, Map.class).get();
        return bind(clazz, properties);
    }
}
