package amo.lib.exception;


import amo.lib.common.EventTypeEnum;
import amo.lib.common.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public JsonData handlerDefaultException(CustomException e)
    {
        return JsonData.failed(EventTypeEnum.ApiError.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonData handlerDefaultException(Exception e)
    {
        return JsonData.failed(EventTypeEnum.ApiError.getCode(), e.getMessage());
    }

    private JsonData buildResponse(EventTypeEnum errorCode, String message, Object data) {
        JsonData returnJson = new JsonData();
        returnJson.code(errorCode.getCode());
        returnJson.message(message);
        returnJson.data(data);
        return returnJson;
    }
}