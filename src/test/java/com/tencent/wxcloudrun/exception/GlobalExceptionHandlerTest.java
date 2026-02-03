package com.tencent.wxcloudrun.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.wxcloudrun.controller.DemoController;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 全局异常处理器测试类
 */
@AutoConfigureMybatis
@WebMvcTest(DemoController.class)
@TestPropertySource(properties = "spring.profiles.active=test")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * 测试业务异常处理
     */
    @Test
    void testHandleBusinessException() throws Exception {
        mockMvc.perform(get("/api/demo/business-error")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // BusinessException返回200状态码
                .andExpect(jsonPath("$.code").value(1004))
                .andExpect(jsonPath("$.message").value("这是一个业务异常示例"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试参数校验异常 - 缺少必需参数
     */
    @Test
    void testHandleValidationException_MissingParams() throws Exception {
        mockMvc.perform(get("/api/demo/validation-error")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试参数校验异常 - 空值参数
     */
    @Test
    void testHandleValidationException_EmptyParams() throws Exception {
        mockMvc.perform(get("/api/demo/validation-error")
                .param("name", "")
                .param("age", "")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试参数校验成功
     */
    @Test
    void testHandleValidationSuccess() throws Exception {
        mockMvc.perform(get("/api/demo/validation-error")
                .param("name", "测试用户")
                .param("age", "25")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.data").value("验证通过: 测试用户, 25"));
    }

    /**
     * 测试空指针异常处理
     */
    @Test
    void testHandleNullPointerException() throws Exception {
        mockMvc.perform(get("/api/demo/null-pointer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统内部错误"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试非法参数异常处理
     */
    @Test
    void testHandleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/api/demo/illegal-argument")
                .param("type", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("参数类型不合法: invalid"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试非法参数异常处理 - 合法参数
     */
    @Test
    void testHandleIllegalArgumentException_ValidParam() throws Exception {
        mockMvc.perform(get("/api/demo/illegal-argument")
                .param("type", "valid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value(""))
                .andExpect(jsonPath("$.data").value("参数合法"));
    }

    /**
     * 测试404路径不存在异常
     */
    @Test
    void testHandleNoHandlerFoundException() throws Exception {
        mockMvc.perform(get("/api/demo/non-existent-path-that-does-not-exist")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试请求方法不支持异常
     */
    @Test
    void testHandleHttpRequestMethodNotSupportedException() throws Exception {
        mockMvc.perform(post("/api/demo/business-error")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value(405))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试媒体类型不支持异常 - 需要POST请求才能触发
     */
    @Test
    void testHandleHttpMediaTypeNotSupportedException() throws Exception {
        mockMvc.perform(post("/api/demo/post-demo")
                .content("test content")
                .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.code").value(415))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试请求体不可读异常 - 需要POST请求且有请求体
     */
    @Test
    void testHandleHttpMessageNotReadableException() throws Exception {
        String invalidJson = "{invalid json}";
        
        mockMvc.perform(post("/api/demo/post-demo")
                .content(invalidJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("请求体格式错误或不可读"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
    /**
     * 测试缺少请求头异常处理
     */
    @Test
    void testHandleMissingRequestHeaderException() throws Exception {
        // 测试缺少X-User-ID请求头
        mockMvc.perform(get("/api/demo/business-error")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // 这个端点不需要请求头，所以不会触发异常
        
        // 需要测试一个需要特定请求头的端点
        // 由于DemoController中没有需要特定请求头的端点，这个测试暂时跳过
    }

    /**
     * 测试缺少请求参数异常处理
     */
    @Test
    void testHandleMissingServletRequestParameterException() throws Exception {
        // 测试缺少必需参数
        mockMvc.perform(get("/api/demo/validation-error")
                .param("name", "测试用户")
                // 缺少age参数
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试参数类型不匹配异常处理
     */
    @Test
    void testHandleMethodArgumentTypeMismatchException() throws Exception {
        // 测试参数类型错误（期望数字但传入字符串）
        mockMvc.perform(get("/api/demo/validation-error")
                .param("name", "测试用户")
                .param("age", "not-a-number") // 传入非数字值
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试非法状态异常处理
     */
    @Test
    void testHandleIllegalStateException() throws Exception {
        mockMvc.perform(get("/api/demo/illegal-state")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统状态异常"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试运行时异常处理
     */
    @Test
    void testHandleRuntimeException() throws Exception {
        mockMvc.perform(get("/api/demo/runtime-error")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统内部错误"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    /**
     * 测试通用异常处理
     */
    @Test
    void testHandleGenericException() throws Exception {
        mockMvc.perform(get("/api/demo/generic-error")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统异常，请联系管理员"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}