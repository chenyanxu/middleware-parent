package com.kalix.middleware.workflow.api.model;

//import com.kalix.framework.core.api.persistence.PersistentEntity;

import com.kalix.framework.core.api.persistence.PersistentEntity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;

/**
 * @类描述：工作流业务数据抽象基类
 * @创建人：sunlf
 * @创建时间：2014-9-9 下午1:01:59
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class WorkflowEntity extends PersistentEntity {
    private String title; //流程名称
    private Long orgId; //组织结构id
    private String orgName; //组织结构名称
    private String processInstanceId;//流程实例id
    private String currentNode;//当前环节
    private short status = WorkflowStaus.INACTIVE;
    private String auditResult="流程尚未启动";//审批最终结果
    private String businessNo = ""; //业务编号

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String instanceId) {
        this.processInstanceId = instanceId;
    }

    public short getStatus() {
        return this.status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(String currentNode) {
        this.currentNode = currentNode;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
