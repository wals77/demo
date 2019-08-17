package com.springboot.demo.model;

import com.springboot.demo.annotation.MyConstraint;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class RequestDataDO {

    @NotNull(message = "uid不能为空")
    private String uid;

    @NotNull(message = "spm不能为空")
    private String spm;

    @Min(value = 5, message = "pid范围为5-10") @Max(value = 10, message = "pid范围为5-10")
    private Long pid;

    @MyConstraint(name = "ldq", message = "名称不对")
    private String name;

}
