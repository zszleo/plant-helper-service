package com.tencent.wxcloudrun.exception;

import com.tencent.wxcloudrun.constant.CodeEnum;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * @author zszleon
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: URI={}, Code={}, Message={}", request.getRequestURI(), e.getCode(), e.getMessage());
        return ApiResponse.of(e.getCode(), e.getMessage(), null);
    }

    /**
     * 参数校验异常处理（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数校验异常: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return ApiResponse.error(errorMessage);
    }

    /**
     * 参数绑定异常处理
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBindException(BindException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数绑定异常: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return ApiResponse.error(errorMessage);
    }


    /**
     * 约束违反异常处理（@Validated）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("约束违反异常: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return ApiResponse.error(errorMessage);
    }

    /**
     * 缺少请求头异常处理
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMissingRequestHeaderException(MissingRequestHeaderException e, HttpServletRequest request) {
        String errorMessage = String.format("缺少必需的请求头: %s", e.getHeaderName());
        log.warn("缺少请求头异常: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return ApiResponse.error(errorMessage);
    }

    /**
     * 缺少请求参数异常处理
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String errorMessage = String.format("缺少必需的请求参数: %s", e.getParameterName());
        log.warn("缺少请求参数异常: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return ApiResponse.error(errorMessage);
    }

    /**
     * 参数类型不匹配异常处理
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String errorMessage = String.format("参数类型不匹配: %s 应为 %s 类型", e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        log.warn("参数类型不匹配异常: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return ApiResponse.error(errorMessage);
    }

    /**
     * 请求体不可读异常处理
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("请求体不可读异常: URI={}, Message={}", request.getRequestURI(), e.getMessage());
        return ApiResponse.error("请求体格式错误或不可读");
    }

    /**
     * 请求方法不支持异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String supportedMethods = e.getSupportedMethods() != null ? String.join(", ", e.getSupportedMethods()) : "未知";
        String errorMessage = String.format("不支持 %s 请求方法，支持的方法: %s", e.getMethod(), supportedMethods);
        log.warn("请求方法不支持异常: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return ApiResponse.of(CodeEnum.METHOD_NOT_ALLOWED.getCode(), errorMessage, null);
    }

    /**
     * 媒体类型不支持异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiResponse<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        String errorMessage = String.format("不支持的媒体类型: %s", e.getContentType());
        log.warn("媒体类型不支持异常: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return ApiResponse.of(CodeEnum.UNSUPPORTED_MEDIA_TYPE.getCode(), errorMessage, null);
    }

    /**
     * 文件上传大小超限异常处理
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ApiResponse<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.warn("文件上传大小超限异常: URI={}, Message={}", request.getRequestURI(), e.getMessage());
        return ApiResponse.of(CodeEnum.PAYLOAD_TOO_LARGE.getCode(), "上传文件大小超过限制", null);
    }

    /**
     * 404异常处理
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String errorMessage = String.format("请求的路径不存在: %s", e.getRequestURL());
        log.warn("404异常: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return ApiResponse.of(CodeEnum.NOT_FOUND.getCode(), errorMessage, null);
    }

    /**
     * 非法参数异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("非法参数异常: URI={}, Message={}", request.getRequestURI(), e.getMessage());
        return ApiResponse.error(e.getMessage());
    }

    /**
     * 非法状态异常处理
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        log.error("非法状态异常: URI={}, Message={}", request.getRequestURI(), e.getMessage(), e);
        return ApiResponse.of(CodeEnum.INTERNAL_SERVER_ERROR.getCode(), "系统状态异常", null);
    }

    /**
     * 空指针异常处理
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常: URI={}", request.getRequestURI(), e);
        return ApiResponse.of(CodeEnum.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误", null);
    }

    /**
     * 运行时异常处理
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: URI={}, Message={}", request.getRequestURI(), e.getMessage(), e);
        return ApiResponse.of(CodeEnum.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误", null);
    }

    /**
     * 其他异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: URI={}, Message={}", request.getRequestURI(), e.getMessage(), e);
        return ApiResponse.of(CodeEnum.INTERNAL_SERVER_ERROR.getCode(), "系统异常，请联系管理员", null);
    }
}
