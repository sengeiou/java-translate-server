package com.cretin.webdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * APP国际化翻译 模块对应表
 * </p>
 *
 * @author Cretin
 * @since 2022-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TbStrTranslateModule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 路径值
     */
    private String path;

    /**
     * 路径标签
     */
    private String pathLabel;

    private LocalDateTime addTime;


}
