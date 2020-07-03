package amo.lib.dynamic;

public class DynamicDataContextHolder {
    /*
     * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */

    /*
     *ThreadLocal为线程绑定的,只绑定当前线程,如果需要使用异步或者多线程,改用InheritableThreadLocal,可以使子线程获取父线程的附带信息,但是使用线程池
     * 时需要使用阿里的InheritableThreadLocal
     */
    private static final ThreadLocal<String> siteContextHolder = new ThreadLocal<String>();

    /*
     * 原始Site,用于Site切换时进行重置
     */
    private static final ThreadLocal<String> originalSiteContextHolder = new ThreadLocal<String>();

    /*
     * 数据库链接
     */
    private static final ThreadLocal<String> dataSourceContextHolder = new ThreadLocal<String>();

    /*
     * 设置当前线程的Site
     */
    public static void setSite(String site) {
        originalSiteContextHolder.set(siteContextHolder.get());
        siteContextHolder.set(site);
    }

    /*
     * 获取当前线程的Site
     */
    public static String getSite() {
        return siteContextHolder.get();
    }

    /*
     * 重置Site,在进行Site切换时,处理完要切回Site,如果嵌套多次切换,需要手动切回
     */
    public static void resetSite(){
        siteContextHolder.set(originalSiteContextHolder.get());
        originalSiteContextHolder.remove();
    }

    /*
     * 切换数据源
     */
    public static void setDataSourceKey(String dataSourceKey) {
        dataSourceContextHolder.set(dataSourceKey);
    }

    public static String getDataSourceKey() {
        return dataSourceContextHolder.get();
    }

    public static void clearDataSourceKey() {
        dataSourceContextHolder.remove();
    }
}
