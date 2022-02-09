package com.cretin.webdb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * APP国际化翻译用户表
 * </p>
 *
 * @author Cretin
 * @since 2022-02-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TbStrTranslateUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 状态  0 正常 1 不可用
     */
    private Integer status;

    /**
     * 用户token
     */
    private String token;

    private LocalDateTime updateTime;


}
