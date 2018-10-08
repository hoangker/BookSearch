package com.tin.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;


import com.tin.model.Book;
import com.tin.model.Search;
import com.tin.model.SearchResult;
import com.tin.service.BookServiceImpl;


@Controller
@SessionAttributes({"search"})
public class SearchController {

	@Autowired
	private BookServiceImpl bookService;
	
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public String addSearch(Model model) {
		Search search = new Search();
		search.setTitle("");
		model.addAttribute("search", search);
		
		return "search";
	}
	
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public String updateSearch(@ModelAttribute("search") Search search) 
	{
		System.out.println("Search for book:" + search.getTitle());
		return "redirect: results.html";
	}
	
	@RequestMapping(value = "results", method = RequestMethod.GET)
	public String showResults(@ModelAttribute("search") Search search, Model model) 
	{
		SearchResult results = new SearchResult();
		bookService.getBookList(search.getTitle(), 1, results);
		results.setCurrentPage(1);
		results.setSearch(search);
		results.setSortBy("title");
		results.setSortOrder("ASC");
		model.addAttribute("results", results);
		return "results";
	}
	
	@RequestMapping(value = "results", method = RequestMethod.POST)
	public @ResponseBody String updateResults(@ModelAttribute("results") SearchResult results, @RequestParam String query, @RequestParam int pageNumber, @RequestParam String sortBy, @RequestParam String sortOrder, Model model)
	{
		//SearchResult results = new SearchResult();
		System.out.println("sortBy:"+sortBy+";sortOrder:"+sortOrder);
		results.setSortBy(sortBy);
		results.setSortOrder(sortOrder);
		model.addAttribute("results", results);
		return bookService.getBookListJSON(query, pageNumber, results);
	}
	
}

