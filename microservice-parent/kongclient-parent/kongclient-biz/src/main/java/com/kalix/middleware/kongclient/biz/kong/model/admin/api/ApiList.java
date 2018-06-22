package com.kalix.middleware.kongclient.biz.kong.model.admin.api;

import com.kalix.middleware.kongclient.biz.kong.model.common.AbstractEntityList;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 13/06/17.
 */
//@Data
public class ApiList extends AbstractEntityList {
    Long total;
    String next;
    List<Api> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<Api> getData() {
        return data;
    }

    public void setData(List<Api> data) {
        this.data = data;
    }
}
