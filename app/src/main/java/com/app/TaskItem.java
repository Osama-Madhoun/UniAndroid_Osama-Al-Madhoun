package com.app;

public class TaskItem {
    private String titleTask;
    private String taskID;
    private String taskDesc;

    public TaskItem() {
    }

    public TaskItem(String titleTask, String taskDesc) {
        this.titleTask = titleTask;
        this.taskDesc = taskDesc;
    }

    public void setTitleTask(String titleTask) {
        this.titleTask = titleTask;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTitleTask() {
        return titleTask;
    }

    public String getTaskID() {
        return taskID;
    }

    public String getTaskDesc() {
        return taskDesc;
    }
}
