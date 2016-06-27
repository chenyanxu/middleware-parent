package com.kalix.middleware.workflow.biz;

import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sunlf on 2015/8/1.
 * 用于显示工作流图片的servlet
 * test address http://localhost:8181/image?processInstanceId=5001&processDefinitionId=fireworks:1:3
 */
public class BpmnImageServlet extends BaseBpmnImageServlet {

    private String processDefinitionId;
    private String processInstanceId;
    private String taskId;
    private TaskService taskService;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        InputStream imageStream=null;
        processDefinitionId = request.getParameter("processDefinitionId");
        processInstanceId = request.getParameter("processInstanceId");
        taskId= request.getParameter("taskId");

        if (processDefinitionId != null)//如果传入一个参数，返回流程图片，不带活动节点
            imageStream = getInputStream(processDefinitionId);
        else if (processInstanceId != null) {//带活动节点
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            imageStream = getInputStream(pi.getProcessDefinitionId(), processInstanceId);
        } else if (taskId != null) {//带活动节点
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            processInstanceId=task.getProcessInstanceId();
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            imageStream = getInputStream(pi.getProcessDefinitionId(), processInstanceId);
        }

        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];
            int count;
            while ((count = imageStream.read(buf)) >= 0) {
                try {
                    out.write(buf, 0, count);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(imageStream!=null)
                    imageStream.close();
                if(out!=null) {
                    out.flush();
                    out.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        doGet(request, response);
    }


}

