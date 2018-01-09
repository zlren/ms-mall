package lab.zlren.mall.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * User
 *
 * @author zlren
 * @since 2018-01-06
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id，手机号码
     */
    private Long id;
    private String nickname;
    /**
     * 两次md5
     */
    @JsonIgnore
    private String password;

    @JsonIgnore
    private String salt;
    /**
     * 头像，云存储的id
     */
    private String head;
    /**
     * 注册时间
     */
    @TableField("register_date")
    private Date registerDate;
    /**
     * 上次登录时间
     */
    @TableField("last_login_date")
    private Date lastLoginDate;
    /**
     * 登录次数
     */
    @TableField("login_count")
    private Integer loginCount;
}
