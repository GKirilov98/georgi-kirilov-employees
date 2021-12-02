package com.example.georgikirilovemployees.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Project: georgi-kirilov-employees
 * Created by: GKirilov
 * On: 12/2/2021
 */
public class EmployeeModel implements Serializable {
    private Long empId;
    private Long projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
