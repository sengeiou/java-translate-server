package com.cretin.webcore.utils

import org.joda.time.DateTime
import org.joda.time.Duration
import java.text.SimpleDateFormat
import java.util.*

/**
 * author： deemons
 * date:    2018/4/24
 * desc:    此类是对 Joda Time 的扩展
 * Joda Time 使用参考：https://www.ibm.com/developerworks/cn/java/j-jodatime.html#artdownload
 * http://ylq365.iteye.com/blog/1769680
 * Joda Time 本身非常强大，此处只是针对本项目做了一下扩展
 */
object TimeUtils {

    private val CALENDAR_OF_INSTANCE = Calendar.getInstance()
    private const val SECONDS_OF_1MINUTE = 60
    private const val SECONDS_OF_1HOUR = 60 * 60
    private const val SECONDS_OF_1DAY = 24 * 60 * 60
    private var SECONDS_OF_2DAY = SECONDS_OF_1DAY + CALENDAR_OF_INSTANCE.get(Calendar.HOUR) * SECONDS_OF_1HOUR +
            CALENDAR_OF_INSTANCE.get(Calendar.MINUTE) * SECONDS_OF_1MINUTE + CALENDAR_OF_INSTANCE.get(Calendar.SECOND)

    /**
     * 友好显示时间，包含国际化
     */
    fun convert(time: Long): String {
        var long: Long = time
        if (long.toString().length <= 10) {
            long = time * 1000
        }
        val duration = Duration(long, System.currentTimeMillis())
        val durationSec = duration.standardSeconds.toInt()

        val now = DateTime()
        val startOfToday = DateTime(now.year, now.monthOfYear, now.dayOfMonth, 0, 0, 0)
        val endOfToday = startOfToday.plusDays(1)
        val startOfYesterday = startOfToday.minusDays(1)

        val date = Date(long)

        return when {
            durationSec < SECONDS_OF_1MINUTE -> "刚刚"
            durationSec < SECONDS_OF_1HOUR -> "${duration.standardMinutes.toInt()}分钟前"
            date.after(startOfToday.toDate()) && date.before(endOfToday.toDate()) -> "${duration.standardHours.toInt()}小时前"
            date.after(startOfYesterday.toDate()) && date.before(startOfToday.toDate()) -> "昨天 ${millis2String(time, "HH:mm")}"
            else -> {
                val dateYear = DateTime(long)
                //一年内 显示时间
                return if (DateTime.now().year == dateYear.year) {
                    millis2String(long, "MM/dd HH:mm")
                    //一年外
                } else {
                    millis2String(long, "yyyy/MM/dd HH:mm")
                }
            }
        }
    }

    /**
     * 将注册时间转化成入驻时间
     */
    fun formatRegisterTimeToTimeLength(time: Long): String {
        var long: Long = time
        if (long.toString().length <= 10) {
            long = time * 1000
        }
        val duration = Duration(long, System.currentTimeMillis())
        val durationSec = duration.standardSeconds.toInt()

        val now = DateTime()
        val startOfToday = DateTime(now.year, now.monthOfYear, now.dayOfMonth, 0, 0, 0)
        val endOfToday = startOfToday.plusDays(1)
        val startOfYesterday = startOfToday.minusDays(1)

        val date = Date(long)

        return when {
            durationSec < SECONDS_OF_1MINUTE -> "刚刚"
            durationSec < SECONDS_OF_1HOUR -> "${duration.standardMinutes.toInt()}分钟前"
            date.after(startOfToday.toDate()) && date.before(endOfToday.toDate()) -> "${duration.standardHours.toInt()}小时前"
            date.after(startOfYesterday.toDate()) && date.before(startOfToday.toDate()) -> "昨天"
            durationSec < 30 * 24 * 3600 -> "${durationSec / (24 * 3600)}天"
            durationSec < 365 * 24 * 3600 -> "${durationSec / (30 * 24 * 3600)}个月"
            else -> {
                "${durationSec / (365 * 24 * 3600)}年" + "${if (durationSec % (365 * 24 * 3600) / (30 * 24 * 3600) == 0) 1 else durationSec % (365 * 24 * 3600) / (30 * 24 * 3600)}个月"
            }
        }
    }

    /**
     * 显示日期，不包含时间
     */
    fun convertWithoutTime(time: Long): String {
        var long: Long = time
        if (long.toString().length <= 10) {
            long = time * 1000
        }
        val duration = Duration(long, System.currentTimeMillis())
        val durationSec = duration.standardSeconds.toInt()

        val now = DateTime()
        val startOfToday = DateTime(now.year, now.monthOfYear, now.dayOfMonth, 0, 0, 0)
        val endOfToday = startOfToday.plusDays(1)
        val startOfYesterday = startOfToday.minusDays(1)
        val date = Date(long)

        return when {
            durationSec < SECONDS_OF_1MINUTE -> "刚刚"
            durationSec < SECONDS_OF_1HOUR -> "${duration.standardMinutes.toInt()}分钟前"
            date.after(startOfToday.toDate()) && date.before(endOfToday.toDate()) -> "${duration.standardHours.toInt()}小时前"
            date.after(startOfYesterday.toDate()) && date.before(startOfToday.toDate()) -> "月"
            else -> {
                val dateYear = DateTime(long)
                //一年内
                return if (DateTime.now().year == dateYear.year) {
                    millis2String(long, "MM/dd HH:mm")
                    //一年外
                } else {
                    millis2String(long, "yyyy/MM/dd HH:mm")
                }
            }
        }
    }

    fun millis2String(millis: Long, pattern: String): String {
        return millis2String(millis, pattern, Locale.getDefault())
    }

    /**
     * 将时间戳转为时间字符串
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    private fun millis2String(millis: Long, pattern: String, locale: Locale): String {
        return SimpleDateFormat(pattern, locale).format(Date(millis))
    }

    /**
     * 是否在七天内
     * 传入比对的时间
     */
    fun isSevenDays(time: Long): Boolean {
        val duration = Duration(time, System.currentTimeMillis())
        val durationSec = duration.standardSeconds.toInt()
        return durationSec < SECONDS_OF_1DAY * 7
    }

    /**
     * 获取本周的时间起始时间和终止时间 每一周从周一开始计算到周日 返回的是两个时间节点 单位秒
     */
    fun thisWeekTimeRangeSecond(): LongArray {
        val now = DateTime.now()
        var todayMorning = DateTime(now.year, now.monthOfYear, now.dayOfMonth, 0, 0, 0)
        var index = now.dayOfWeek - 1
        val beforeDay = todayMorning.minusDays(index)
        return longArrayOf(beforeDay.millis / 1000, now.millis / 1000)
    }

    /**
     * 获取上周的时间起始时间和终止时间 每一周从周一开始计算到周日 返回的是两个时间节点 单位秒
     */
    fun lastWeekTimeRangeSecond(): LongArray {
        val now = DateTime.now().minusWeeks(1)
        var todayMorning = DateTime(now.year, now.monthOfYear, now.dayOfMonth, 0, 0, 0)
        var index = now.dayOfWeek - 1
        val beforeDay = todayMorning.minusDays(index)
        val afterDays = beforeDay.plusDays(7)
        return longArrayOf(beforeDay.millis / 1000, afterDays.millis / 1000 - 1)
    }

    /**
     * 获取本月的时间起始时间和终止时间 返回的是两个时间节点 单位秒
     */
    fun thisMonthTimeRangeSecond(): LongArray {
        val now = DateTime.now()
        var todayMorning = DateTime(now.year, now.monthOfYear, now.dayOfMonth, 0, 0, 0)
        var index = now.dayOfMonth - 1
        val beforeDay = todayMorning.minusDays(index)
        return longArrayOf(beforeDay.millis / 1000, now.millis / 1000)
    }

    /**
     * 获取本月的时间起始时间和终止时间 返回的是两个时间节点 单位秒
     */
    fun lastMonthTimeRangeSecond(): LongArray {
        val now = DateTime.now().minusMonths(1)
        var lastMonthFirstDay = DateTime(now.year, now.monthOfYear, 1, 0, 0, 0)
        return longArrayOf(lastMonthFirstDay.millis / 1000, lastMonthFirstDay.plusMonths(1).millis / 1000 - 1)
    }

    /**
     * 获取今日的时间起始时间和终止时间 返回的是两个时间节点 单位秒
     */
    fun todayTimeRangeSecond(): LongArray {
        val now = DateTime.now()
        var todayMorning = DateTime(now.year, now.monthOfYear, now.dayOfMonth, 0, 0, 0)
        return longArrayOf(todayMorning.millis / 1000, now.millis / 1000)
    }

    /**
     * 获取昨日的时间起始时间和终止时间 返回的是两个时间节点 单位秒
     */
    fun yesterdayTimeRangeSecond(): LongArray {
        val now = DateTime.now()
        var todayMorning = DateTime(now.year, now.monthOfYear, now.dayOfMonth, 0, 0, 0)
        return longArrayOf(todayMorning.minusDays(1).millis / 1000, todayMorning.millis / 1000 - 1)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(formatRegisterTimeToTimeLength(DateTime.now().minusSeconds(10).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusHours(1).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusDays(1).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusWeeks(1).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusMonths(1).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusDays(50).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusDays(300).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusDays(400).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusDays(500).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusDays(600).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusDays(700).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusDays(800).millis))
        println(formatRegisterTimeToTimeLength(DateTime.now().minusDays(365 * 3).millis))
    }
}
