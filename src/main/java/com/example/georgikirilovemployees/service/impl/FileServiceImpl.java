package com.example.georgikirilovemployees.service.impl;

import com.example.georgikirilovemployees.model.EmployeeModel;
import com.example.georgikirilovemployees.service.IFiletService;
import com.example.georgikirilovemployees.view.EmployeeOutView;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Project: georgi-kirilov-employees
 * Created by: GKirilov
 * On: 12/3/2021
 */
@Service
public class FileServiceImpl implements IFiletService {
    private final int combinationsLength = 2;
    private final int[] arr = new int[combinationsLength];
    private final List<EmployeeOutView> outViews = new ArrayList<>();
    private final Map<String, Integer> couplesPkIdTotalWorkDays = new HashMap<>();

    @Override
    public List<EmployeeOutView> getTopCoupleEmployee(InputStream inputStream) throws IllegalArgumentException {
        try {
            outViews.clear();

            List<EmployeeModel> employeeModels = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while (reader.ready()) {
                    String line = reader.readLine();
                    if (line.trim().equals("")) {
                        continue;
                    }

                    String[] params = line.split(", ");
                    EmployeeModel employee = new EmployeeModel();
                    if (params[0] != null) {
                        employee.setEmpId(Long.parseLong(params[0]));
                    }

                    if (params[1] != null) {
                        employee.setProjectId(Long.parseLong(params[1]));
                    }

                    try {
                        if (params[2] != null) {
                            String s = params[2].replaceAll("[ /,.]", "-");
                            employee.setDateFrom(LocalDate.parse(s));
                        }

                        if (params[3] != null) {
                            if (params[3].equals("NULL")) {
                                employee.setDateTo(LocalDate.now());
                            } else {
                                String s = params[3].replaceAll("[ /,.]", "-");
                                employee.setDateTo(LocalDate.parse(s));
                            }
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Error with date format!");
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
            Set<String> maxPkIds = new HashSet<>();
            int maxValueInMap=(Collections.max(couplesPkIdTotalWorkDays.values()));
            for (Map.Entry<String, Integer> entry : couplesPkIdTotalWorkDays.entrySet()) {
                if (entry.getValue()==maxValueInMap) {
                    maxPkIds.add(entry.getKey());
                }
            }

            return outViews.stream()
                    .filter(e -> maxPkIds.contains(e.getPkId()))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while file processing!");
        }
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
            EmployeeModel tempEmpl1 = emplArray[arr[0]];
            EmployeeModel tempEmpl2 = emplArray[arr[1]];
            if (!tempEmpl1.getProjectId().equals(tempEmpl2.getProjectId())) {
                return;
            }

            EmployeeModel emp1;
            EmployeeModel emp2;
            if (tempEmpl1.getEmpId() < tempEmpl2.getEmpId()) {
                emp1 = tempEmpl1;
                emp2 = tempEmpl2;
            } else if (tempEmpl1.getEmpId() > tempEmpl2.getEmpId()) {
                emp1 = tempEmpl2;
                emp2 = tempEmpl1;
            } else {
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
                String pkId = emp1.getEmpId() + ";" + emp2.getEmpId();
                view.setPkId(pkId);
                if (couplesPkIdTotalWorkDays.containsKey(pkId)) {
                    couplesPkIdTotalWorkDays.put(pkId, workDays + couplesPkIdTotalWorkDays.get(pkId));
                } else {
                    couplesPkIdTotalWorkDays.put(pkId, workDays);
                }
            }
        } else {
            for (int i = start; i < emplArraySize; i++) {
                arr[index] = i;
                couples(index + 1, i + 1, emplArraySize, emplArray);
            }
        }
    }
}
