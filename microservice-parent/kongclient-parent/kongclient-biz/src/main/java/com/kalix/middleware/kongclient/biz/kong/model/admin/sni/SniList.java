package com.kalix.middleware.kongclient.biz.kong.model.admin.sni;

import com.kalix.middleware.kongclient.biz.kong.model.common.AbstractEntityList;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 13/06/17.
 */
//@Data
public class SniList extends AbstractEntityList {
    Long total;
    List<Sni> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Sni> getData() {
        return data;
    }

    public void setData(List<Sni> data) {
        this.data = data;
    }
}
