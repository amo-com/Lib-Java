package amo.lib.common;

import java.util.LinkedHashMap;

public class JsonData<T> extends LinkedHashMap<String, Object> {
    private static final String codeName = "code";
    private static final String messageName = "message";
    private static final String dataName = "data";
    private Integer code;
    private String message;
    private T data;

    public JsonData result(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public static JsonData success() {
        JsonData jsonData = new JsonData();
        jsonData.put(codeName, EventTypeEnum.Success.getCode());
        return jsonData;
    }

    public static JsonData success(Integer code) {
        JsonData jsonData = new JsonData();
        jsonData.put(codeName, code);
        return jsonData;
    }

    public static JsonData success(Object data) {
        JsonData jsonData = new JsonData();
        jsonData.put(codeName, EventTypeEnum.Success.getCode());
        jsonData.put(dataName, data);
        return jsonData;
    }

    public static JsonData success(Object data, Integer code) {
        JsonData jsonData = new JsonData();
        jsonData.put(codeName, code);
        jsonData.put(dataName, data);
        return jsonData;
    }

    public static JsonData success(Object data, Integer code, String message) {
        JsonData jsonData = new JsonData();
        jsonData.put(codeName, code);
        jsonData.put(dataName, data);
        jsonData.put(messageName, message);
        return jsonData;
    }

    public static JsonData failed() {
        JsonData jsonData = new JsonData();
        jsonData.put(codeName, EventTypeEnum.ApiError.getCode());
        return jsonData;
    }

    public static JsonData failed(Integer code) {
        JsonData jsonData = new JsonData();
        jsonData.put(codeName, code);
        return jsonData;
    }

    public static JsonData failed(Integer code, String message) {
        JsonData jsonData = new JsonData();
        jsonData.put(codeName, code);
        jsonData.put(messageName, message);
        return jsonData;
    }

    public JsonData code(int code) {
        return result(codeName, code);
    }

    public JsonData message(String message) {
        return result(messageName, message);
    }

    public JsonData data(T data) {
        return result(dataName, data);
    }

    public Integer getCode() {
        Integer code = (Integer) this.get(codeName);
        return code;
    }

    public String getMessage() {
        Object message = this.get(messageName);
        return message == null ? "" : message.toString();
    }

    public T getData() {
        Object obj = this.get(dataName);
        return obj == null ? null : (T) obj;
    }

    @Override
    public String toString() {
        return "{\"" + codeName + "\":\"" + code + "\",\"" + messageName + "\":\"" + message + "\",\"" + dataName + "\":\"" + data + "\"}";
    }
}