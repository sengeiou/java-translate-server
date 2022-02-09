package com.cretin.webdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * APP国际化翻译表 操作日志
 * </p>
 *
 * @author Cretin
 * @since 2022-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TbStrTranslateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作者
     */
    private String translateUser;

    /**
     * 翻译内容
     */
    private String translateEn;

    /**
     * 翻译条目id
     */
    private Integer translateId;

    private LocalDateTime updateTime;


}
