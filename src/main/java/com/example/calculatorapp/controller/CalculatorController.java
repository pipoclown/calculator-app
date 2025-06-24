package com.example.calculatorapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalculatorController {

    @Value("${COMPANY_NAME:Default Company}")
    private String companyName;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("companyName", companyName);
        return "index";
    }

	@PostMapping("/calculate")
	public String calculate(
			@RequestParam("number1") double number1,
			@RequestParam("number2") double number2,
			@RequestParam("operation") String operation,
			Model model) {
// Comment
		double result = switch (operation) {
			case "add" -> number1 + number2;
			case "subtract" -> number1 - number2;
			case "multiply" -> number1 * number2;
			//case "divide" -> number2 != 0 ? number1 / number2 : Double.NaN;
			default -> 0;
		};

		String label = switch (operation) {
			case "add" -> "Add";
			case "subtract" -> "Subtract";
			case "multiply" -> "Multiply";
			//case "divide" -> "Divide";
			default -> "Unknown";
		};

		model.addAttribute("result", result);
		model.addAttribute("operationLabel", label);
		model.addAttribute("companyName", companyName);
		return "index";
	}
}
