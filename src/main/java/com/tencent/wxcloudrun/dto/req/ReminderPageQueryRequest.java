package com.tencent.wxcloudrun.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReminderPageQueryRequest extends PageQueryRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long plantId;
}
