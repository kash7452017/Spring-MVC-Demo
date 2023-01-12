package com.luv2code.springdemo.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {

	// Step.1 創建類別物件
	@RequestMapping("/showForm")
	public String showForm(Model theModel)
	{
		// create a student object
		Student theStudent = new Student();
		
		// add student object to model 
		theModel.addAttribute("student", theStudent);
		
		// Step.2 創建student-form頁面
		return "student-form";
	}
	
	// Step.3 創建表單送出動作，回傳設定之值並填入變數中存取
	@RequestMapping("/processForm")
	public String processForm(@ModelAttribute("student") Student theStudent)
	{
		// log the input data
		System.out.println("theStudent: " + theStudent.getFirstName()
							+ " " + theStudent.getLastName());
		
		// Step.4 創建student-confirmation頁面，接收回傳之值並顯示於頁面中
		return "student-confirmation";
	}
}
