package com.imes.opcda.display.uitls;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimestampUtils {

    /**
     * 工具静态方法， 获取时间戳, 将时间Calendar 进行转换, 时分秒 分别为 0
     * @return 返回一个时间戳
     */
    public static Timestamp getTimeStamp(Calendar calendar) {
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

}
