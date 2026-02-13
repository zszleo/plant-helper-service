package com.tencent.wxcloudrun.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "处理结果")
public class ProcessingResultDTO {
    
    @Schema(description = "处理动作", example = "show_error_toast")
    private String action;
    
    @Schema(description = "是否可以重试", example = "false")
    private Boolean canRetry;
    
    @Schema(description = "是否已处理", example = "true")
    private Boolean handled;
}