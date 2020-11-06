package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExpenditureController {
	@Autowired
	private ExpenditureRepo repo;
	
	@Autowired
	private ExpendService service;
	
	@GetMapping("/")
	public String homepage(Model model) {
		return findPaginated(1,  "id", "desc", model);
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 8;
		Page<Expenditure> page = service.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Expenditure> alldata = page.getContent();
		
		if (alldata.size() == 0) {
			return not_found(model);
		}
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("alldata", alldata);
		
		// sorting
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		// add new record
		Expenditure expenditure = new Expenditure();
		model.addAttribute("expenditure", expenditure);
		
		// statistics
		model.addAttribute("sum", repo.findSumCost());
		model.addAttribute("categories", repo.findAllByCategory());
		//model.addAttribute("bymonth", repo.findAllByMonth());
		return "index";
	}
	
	@PostMapping("/saveExpenditure")
	public String saveExpenditure(@ModelAttribute("expenditure") Expenditure expenditure) {
		repo.save(expenditure);
		return "redirect:/";
	}
	
	@GetMapping("/deleteExpenditure/{id}")
	public String deleteExpenditure(@PathVariable (value="id") long id) {
		repo.deleteById(id);
		return "redirect:/";
	}
	
	@GetMapping("/updateExpenditure/{id}")
	public String updateExpenditure(@PathVariable(value="id") long id, Model model) {
		Expenditure expenditure = service.getExpenditureById(id);
		model.addAttribute("expenditure", expenditure);
		return "update";
	}
	
	@GetMapping("/statistics")
	public String charts(Model model) {
		int num = repo.countall();
		if (num == 0) {
			return not_found(model);
		}
		
		model.addAttribute("sum", repo.findSumCost());
		model.addAttribute("food", repo.findSumFood());
		model.addAttribute("traffic", repo.findSumTraffic());
		model.addAttribute("others", repo.findSumOthers());
		model.addAttribute("bymonth", repo.findAllByMonth());
		
		return "statistics";
	}
	
	@GetMapping("/not_found")
	public String not_found(Model model) {
		Expenditure expenditure = new Expenditure();
		model.addAttribute("expenditure", expenditure);
		return "notfound";
	}
 	
}
