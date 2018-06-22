package com.kalix.middleware.kongclient.biz.kong.model.plugin.authentication.oauth2;

import com.kalix.middleware.kongclient.biz.kong.model.common.AbstractEntityList;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 15/06/17.
 */
//@Data
public class ApplicationList extends AbstractEntityList {

    Long total;

    List<Application> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Application> getData() {
        return data;
    }

    public void setData(List<Application> data) {
        this.data = data;
    }
}
