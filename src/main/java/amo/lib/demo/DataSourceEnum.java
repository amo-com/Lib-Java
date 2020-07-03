package amo.lib.demo;

public enum DataSourceEnum {
    Auto(1, "auto", "Auto公共库"),
    Common(2, "common", "Common公共配置信息库"),
    Record(3, "record", "记录库"),
    Catalog(4, "%s.catalog","目录库"),
    Part(5, "%s.part", "目录衍生Part库"),
    Operate(6, "%s.operate","运营库,PM表,UserVehicle"),
    ;

    private final Integer type;
    private final String name;
    private final String desc;

    DataSourceEnum(Integer type, String name, String desc) {
        this.type = type;
        this.name = name;
        this.desc = desc;
    }

    public String GetDataSourceKey(String site)
    {
        String siteLower = site.toLowerCase();
        return String.format(name, siteLower);
    }
}
