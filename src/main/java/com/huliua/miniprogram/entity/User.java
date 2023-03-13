package com.huliua.miniprogram.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "T_USER")
@ApiModel("用户实体类")
public class User {

    @ApiModelProperty("wid")
    private String wid;

    @ApiModelProperty("姓名")
    private String username;

    @TableId(type = IdType.INPUT)
    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("邮箱地址")
    private String email;
}
