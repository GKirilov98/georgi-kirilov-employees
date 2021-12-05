package com.example.georgikirilovemployees.view;

/**
 * Project: georgi-kirilov-employees
 * Created by: GKirilov
 * On: 12/2/2021
 */
public class EmployeeOutView {
    private String pkId;
    private Long empId1;
    private Long empId2;
    private Long projectId;
    private Integer workDays;

    public Long getEmpId1() {
        return empId1;
    }

    public void setEmpId1(Long empId1) {
        this.empId1 = empId1;
    }

    public Long getEmpId2() {
        return empId2;
    }

    public void setEmpId2(Long empId2) {
        this.empId2 = empId2;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Integer workDays) {
        this.workDays = workDays;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    @Override
    public String toString() {
        return "EmployeeOutView{" +
                "empId1=" + empId1 +
                ", empId2=" + empId2 +
                ", projectId=" + projectId +
                ", workDays=" + workDays +
                '}';
    }
}
