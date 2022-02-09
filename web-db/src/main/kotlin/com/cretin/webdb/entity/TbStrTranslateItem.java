package com.cretin.webdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * APP国际化翻译表
 * </p>
 *
 * @author Cretin
 * @since 2022-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TbStrTranslateItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * Android key
     */
    private String androidKey;

    /**
     * Android 包名 用于区分字符串来源
     */
    private String androidPackage;

    /**
     * Android 源内容
     */
    private String androidSource;

    /**
     * iOS key
     */
    private String iosKey;

    /**
     * iOS 包名 用于区分字符串来源
     */
    private String iosPackage;

    /**
     * iOS 源内容
     */
    private String iosSource;

    /**
     * 翻译前的源中文内容
     */
    private String translateSource;

    /**
     * 翻译后的英文内容
     */
    private String translateEn;

    /**
     * 翻译后的繁体内容
     */
    private String translateTw;

    private LocalDateTime addTime;

    private LocalDateTime updateTime;

    /**
     * 合并状态 1 正常 0 有冲突
     */
    private Integer mergeStatus;

    /**
     * app_id 字符串 不是数字
     */
    private String appId;

    /**
     * 确认状态 0 未确定 1 已确定
     */
    private Integer confirmStatus;


}
