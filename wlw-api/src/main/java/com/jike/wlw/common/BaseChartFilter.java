package com.jike.wlw.common;

import com.geeker123.rumba.commons.util.DateUtil;
import com.geeker123.rumba.jpa.api.range.DateRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2021，所有权利保留。
 * <p>
 * 修改历史：
 * 2021/11/8 15:32- zhengzhoudong - 创建。
 */
@Setter
@Getter
@ApiModel("图表查询条件基类")
public class BaseChartFilter implements Serializable {
    private static final long serialVersionUID = -5961937130891336510L;

    public static final String DATE_YEAR_FORMAT = "yyyy"; //年 2021
    public static final String DATE_MONTH_FORMAT = "M"; //月 1-12
    public static final String DATE_MONTH_DAY_FORMAT = "MM-d"; //月和日 11-5 11-17
    public static final String DATE_YEAR_MONTH_FORMAT = "yyyy-MM"; //月和日 11-5 11-17
    public static final String DATE_DAY_FORMAT = "d";//日 1-31
    public static final String DATE_DAY2_FORMAT = "dd";//日 01-31
    public static final String DATE_HOUR_FORMAT = "H";//小时 1-24

    @ApiModelProperty("时间格式")
    private String timeFormat;
    @ApiModelProperty("时间范围")
    private DateRange dateRange;
    @ApiModelProperty("时间范围类型")
    private DateRangeType type;
    @ApiModelProperty("常用时间范围")
    private CommonDateRange commonDateRange;
    @ApiModelProperty("销售大区 BU等于")
    private String bu;
    @ApiModelProperty("销售大区 BU包含于")
    private List<String> buIn;

    public DateRangeType getRangeType() {
        if (type != null) {
            return type;
        }

        if (dateRange == null || dateRange.getBeginDate() == null || dateRange.getEndDate() == null) {
            return DateRangeType.YEAR;
        }

        int betweenDays = DateUtil.getBetweenDays(dateRange.getEndDate(), dateRange.getBeginDate());
        if (betweenDays == 0) {
            type = DateRangeType.HOUR;
        } else if (betweenDays > 0 && betweenDays <= 62) {
            type = DateRangeType.DAY;
        } else if (betweenDays > 62 && betweenDays <= 365) {
            type = DateRangeType.MONTH;
        } else {
            type = DateRangeType.YEAR;
        }

        return type;
    }
}
