# workflow-parent
## 工作流分类
    <definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
                 xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"
                 typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath"
                 targetNamespace="http://www.activiti.org/admin">
通过targetNamespace="http://www.activiti.org/admin"，后面admin为关键字。
## 工作流图标
  位于oa-parent\core-parent\oa-core-extjs\src\main\webapp\resources下，每个工作流流程需要增加一个分类图标。
## 上级领导审批
    <userTask id="depUser" name="正处级领导审核" activiti:formKey="audit.form">
        <documentation>申请部门正处级领导审核</documentation>
        <extensionElements>
        <activiti:formProperty id="accepted" name="判断环节" type="boolean" variable="accepted"></activiti:formProperty>
        <activiti:taskListener event="create" class="com.kalix.middleware.workflow.engine.listener.LeaderListener"/>
        </extensionElements>
    </userTask>
  通过<activiti:taskListener event="create" class="com.kalix.middleware.workflow.engine.listener.LeaderListener"/>即可注册该审批环节的职务为
  “上级领导”。
