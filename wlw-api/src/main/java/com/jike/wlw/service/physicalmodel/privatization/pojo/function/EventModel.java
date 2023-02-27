package com.jike.wlw.service.physicalmodel.privatization.pojo.function;

import com.jike.wlw.service.physicalmodel.CallType;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.EventType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: eventModel
 * @Author RS
 * @Date: 2023/2/27 17:55
 * @Version 1.0
 */

@Getter
@Setter
public class EventModel extends Model {
    private DataType type;
    private String details;
    private String name;
    private String identifier;
    private boolean required;
    private EventType eventType;
    private List<InOutputData> outputData;
}


