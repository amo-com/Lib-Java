package amo.lib.demo;

import java.util.HashMap;

public enum SiteEnum {
    HPN(5,"HPN", "honda"),
    APW(6,"APW", "honda"),
    FPG(11,"FPG","ford")
    ;
    private final Integer key;

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getCatalog() {
        return catalog;
    }

    private final String value;
    private final String catalog;

    private static HashMap<String, SiteEnum> siteEnumHashMap = new HashMap<String, SiteEnum>();

    SiteEnum(Integer key, String value, String catalog){
        this.key=key;
        this.value=value;
        this.catalog=catalog;
    }

    static {
        for (SiteEnum siteEnum : SiteEnum.values()) {
            siteEnumHashMap.put(siteEnum.getValue(), siteEnum);
        }
    }

    public static SiteEnum nameOf(String site) { return siteEnumHashMap.get(site);}
}
