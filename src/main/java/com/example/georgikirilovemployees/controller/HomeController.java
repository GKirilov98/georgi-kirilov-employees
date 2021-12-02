package com.example.georgikirilovemployees.controller;

import com.example.georgikirilovemployees.service.IFiletService;
import com.example.georgikirilovemployees.view.EmployeeOutView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Project: georgi-kirilov-employees
 * Created by: GKirilov
 * On: 12/2/2021
 */
@Controller
public class HomeController {
    private final IFiletService filetService;

    public HomeController(IFiletService filetService) {
        this.filetService = filetService;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @PostMapping("/")
    public ModelAndView indexConfirm(@RequestParam("file") MultipartFile file, ModelAndView modelAndView) throws IOException {
        if (file.isEmpty()) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        EmployeeOutView coupleEmployee = filetService.getTopCoupleEmployee(file.getInputStream());
        modelAndView.addObject("coupleEmployee", coupleEmployee);
        modelAndView.setViewName("index.html");
        return modelAndView;
    }
}
