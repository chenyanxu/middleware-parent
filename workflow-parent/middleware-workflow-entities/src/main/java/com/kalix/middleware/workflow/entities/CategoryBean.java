package com.kalix.middleware.workflow.entities;


import com.kalix.framework.core.api.persistence.PersistentEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * 项目名称:  urgent-project
 * 类描述:    存储系统版本信息实体
 * 创建人:    sunlf
 * 创建时间:  2014/8/11 8:51
 * 修改人:    sunlf
 * 修改时间:  2014/8/11 8:51
 * 修改备注:  [说明本次修改内容]
 */
@Entity
@Table(name = "oa_category")
@Inheritance(strategy = InheritanceType.JOINED)
public class CategoryBean extends PersistentEntity {
    private static final long serialVersionUID = 1L;

    private String name; //分类名称
    private String key; //分類主鍵
    private String icon;//分类图标
    private String description;//分类描述

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
