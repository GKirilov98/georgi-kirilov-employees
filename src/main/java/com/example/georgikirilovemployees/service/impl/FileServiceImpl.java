package com.example.georgikirilovemployees.service.impl;

import com.example.georgikirilovemployees.model.EmployeeModel;
import com.example.georgikirilovemployees.service.IFiletService;
import com.example.georgikirilovemployees.view.EmployeeOutView;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Project: georgi-kirilov-employees
 * Created by: GKirilov
 * On: 12/3/2021
 */
@Service
public class FileServiceImpl implements IFiletService {
    public int combinationsLength = 2;
    public int[] arr = new int[combinationsLength];
    public List<EmployeeOutView> outViews = new ArrayList<>();

    @Override
    public EmployeeOutView getTopCoupleEmployee(InputStream inputStream) throws IOException {
        outViews.clear();

        List<EmployeeModel> employeeModels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (reader.ready()) {
                String line = reader.readLine();
                String[] params = line.split(", ");
                EmployeeModel employee = new EmployeeModel();
                if (params[0] != null) {
                    employee.setEmpId(Long.parseLong(params[0]));
                }

                if (params[1] != null) {
                    employee.setProjectId(Long.parseLong(params[1]));
                }

                if (params[2] != null) {
                    employee.setDateFrom(LocalDate.parse(params[2]));
                }

                if (params[3] != null) {
                    if (params[3].equals("NULL")) {
                        employee.setDateTo(LocalDate.now());
                    } else {
                        employee.setDateTo(LocalDate.parse(params[3]));
                    }
                }

                employeeModels.add(employee);
            }
        }

        inputStream.close();
        EmployeeModel[] emplArray = new EmployeeModel[employeeModels.size()];
        employeeModels.toArray(emplArray);
        //Bubble sort
        int emlArrayLength = emplArray.length;
        for (int left = 0; left < emlArrayLength - 1; left++) {
            for (int right = 0; right < emlArrayLength - left - 1; right++) {
                if (emplArray[right].getProjectId() > emplArray[right + 1].getProjectId()) {
                    EmployeeModel temp = emplArray[right];
                    emplArray[right] = emplArray[right + 1];
                    emplArray[right + 1] = temp;
                }
            }
        }

        //combination
        couples(0, 0, emlArrayLength, emplArray);
        //max
        return outViews
                .stream()
                .max(Comparator.comparing(EmployeeOutView::getWorkDays))
                .orElse(null);
    }

    private int calculateTeamWorkDays(EmployeeModel emp1, EmployeeModel emp2) {
        LocalDate maxDateFrom;
        LocalDate minDateTo;
        int maxDateFromIndex = emp1.getDateFrom().compareTo(emp2.getDateFrom());
        if (maxDateFromIndex >= 0) {
            maxDateFrom = emp1.getDateFrom();
        } else {
            maxDateFrom = emp2.getDateFrom();
        }

        int minDateToIndex = emp1.getDateTo().compareTo(emp2.getDateTo());
        if (minDateToIndex < 0) {
            minDateTo = emp1.getDateTo();
        } else {
            minDateTo = emp2.getDateTo();
        }

        return (int) DAYS.between(maxDateFrom, minDateTo);
    }


    public void couples(int index, int start, int emplArraySize, EmployeeModel[] emplArray) {
        if (index >= combinationsLength) {
            EmployeeModel emp1 = emplArray[arr[0]];
            EmployeeModel emp2 = emplArray[arr[1]];
            if (!emp1.getProjectId().equals(emp2.getProjectId())) {
                return;
            }

            int workDays = calculateTeamWorkDays(emp1, emp2);
            //Ако DateTo > DateFrom -не се зачита
            if (workDays > 0) {
                EmployeeOutView view = new EmployeeOutView();
                view.setEmpId1(emp1.getEmpId());
                view.setEmpId2(emp2.getEmpId());
                view.setProjectId(emp1.getProjectId());
                view.setWorkDays(workDays);
                outViews.add(view);
            }
        } else {
            for (int i = start; i < emplArraySize; i++) {
                arr[index] = i;
                couples(index + 1, i + 1, emplArraySize, emplArray);
            }
        }
    }
}
