package com.kalix.middleware.kongclient.biz.kong.model.admin.plugin;

import com.kalix.middleware.kongclient.biz.kong.model.common.AbstractEntityList;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 13/06/17.
 */
//@Data
public class PluginList extends AbstractEntityList {
    Long total;
    String next;
    List<Plugin> data;

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

    public List<Plugin> getData() {
        return data;
    }

    public void setData(List<Plugin> data) {
        this.data = data;
    }
}
