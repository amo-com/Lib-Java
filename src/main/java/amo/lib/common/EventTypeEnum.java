package amo.lib.common;

/**
 * 状态码,长度六位
 * 1-2位标记类型
 * 第3位标记Level(0,1:Info,2:Debug,3:Trace,4:Warn,5:Error,6:Fatal)
 * 4-6位标记具体错误代码
 *
 * 1-2归属划分:10-59为common公共code,统一定义在common的,其他服务允许定义自己的EventTypeEnum,为了做区分,防止统一,最好是分区间段的,公共的尽量放
 * common中,如catalog service下的EventTypeEnum为71-73,business下75,就可以所有log的EventType都唯一,非必要的,每个服务下唯一即可
 */

public enum EventTypeEnum {
    Success(200, "Success"),
    Redirect(301, "Redirect"),

    ApiError(905101, "Api Error"),
    ;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private final Integer code;
    private final String desc;

    EventTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
