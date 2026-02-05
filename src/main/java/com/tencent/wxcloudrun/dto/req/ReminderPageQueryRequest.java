package com.tencent.wxcloudrun.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "提醒分页查询请求")
public class ReminderPageQueryRequest extends PageQueryRequest {
    @Schema(description = "植物ID（可选）", example = "123456789")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long plantId;
}
