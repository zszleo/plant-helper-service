package com.tencent.wxcloudrun.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecordPageQueryRequest extends PageQueryRequest {
    private Long plantId;
}
