package com.example.georgikirilovemployees.service;

import com.example.georgikirilovemployees.view.EmployeeOutView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Project: georgi-kirilov-employees
 * Created by: GKirilov
 * On: 12/3/2021
 */
public interface IFiletService {
    /**
     * Finds couple with max days work together
     *
     * @param file - input text file as stream
     * @return - out view
     * @throws IllegalArgumentException -
     */
    List<EmployeeOutView> getTopCoupleEmployee(InputStream file) throws IllegalArgumentException;
}
