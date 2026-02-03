package com.tencent.wxcloudrun.dto.resp;

import com.tencent.wxcloudrun.constant.CodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 统一API响应对象
 * @author zszleon
 */
@Data
public class ApiResponse<T> implements Serializable {

  private Integer code;
  private String message;
  private T data;

  private ApiResponse(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> ApiResponse<T> of(int code, String message, T data) {
    return new ApiResponse<>(code, message, data);
  }
  
  public  static <T> ApiResponse<T> ok(String message) {
    return new ApiResponse<>(CodeEnum.SUCCESS.getCode(), message, null);
  }

  public  static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>(CodeEnum.SUCCESS.getCode(), "", data);
  }

  public  static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>(CodeEnum.ERROR.getCode(), message, null);
  }
}
