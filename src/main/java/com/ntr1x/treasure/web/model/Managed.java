package com.ntr1x.treasure.web.model;

import javax.persistence.Transient;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Managed {

    public abstract Long getId();

    @Transient
    @ApiModelProperty(hidden = true)
    private Action action;
}
