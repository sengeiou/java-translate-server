package com.cretin.webdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 全局配置表
 * </p>
 *
 * @author Cretin
 * @since 2022-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TbGlobalConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配置标题
     */
    private String configTitle;

    /**
     * 配置描述
     */
    private String configDesc;

    /**
     * 附加配置1
     */
    private String configExtra1;

    /**
     * 附加配置2
     */
    private String configExtra2;

    /**
     * 附加配置3
     */
    private String configExtra3;

    /**
     * 附加配置4
     */
    private String configExtra4;

    private LocalDateTime updateTime;

    /**
     * 0 翻译工具jar 1 jdk 下载工具地址
     */
    private Integer type;


}
