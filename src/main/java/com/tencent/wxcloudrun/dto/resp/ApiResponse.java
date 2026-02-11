package com.tencent.wxcloudrun.dto.resp;

import com.tencent.wxcloudrun.constant.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import com.tencent.wxcloudrun.dto.resp.PageResponse;

/**
 * 统一API响应对象
 * @author zszleon
 */
@Schema(description = "统一API响应")
@Data
public class ApiResponse<T> implements Serializable {

  @Schema(description = "响应码", example = "200")
  private Integer code;
  
  @Schema(description = "响应消息", example = "操作成功")
  private String message;
  
  @Schema(description = "响应数据")
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

  public static <T> ApiResponse<PageResponse<T>> pageOk(PageResponse<T> data) {
    return new ApiResponse<>(CodeEnum.SUCCESS.getCode(), "", data);
  }
}
