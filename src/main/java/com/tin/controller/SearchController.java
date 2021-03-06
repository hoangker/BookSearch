package com.tin.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tin.model.Search;
import com.tin.model.SearchResult;
import com.tin.service.BookServiceImpl;


@Controller
@SessionAttributes({"search","results"})

public class SearchController {

	private final Logger logger = Logger.getLogger(SearchController.class);
	
	@Autowired
	private BookServiceImpl bookService;
	
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public String addSearch(Model model) {
		logger.debug("debug - search loaded.");
		Search search = new Search();
		SearchResult results = new SearchResult();
		search.setTitle("");
		model.addAttribute("search", search);
		logger.debug("debug - adding attribute search");
		results.setSortBy("title");
		results.setSortOrder("ASC");
		model.addAttribute("results", results);
		return "search";
	}
	
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public @ResponseBody String updateSearch(@ModelAttribute("results") SearchResult results,
			  @ModelAttribute("search") Search search, 
			  @RequestParam String query, @RequestParam int pageNumber, 
			  @RequestParam String sortBy, @RequestParam String sortOrder, 
			  Model model) 
	{
		logger.debug("debug - searching for book...");
		logger.debug("debug - parameters: \"sortBy:"+sortBy+";sortOrder:"+sortOrder);
		search.setTitle(query);
		String json =  bookService.getBookListJSON(query, pageNumber, results);
		/*System.out.println("totalNumberOfPages:"+results.getTotalNumberOfPages());
		model.addAttribute("results", results);*/
		return json;
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
	public @ResponseBody String updateResults(@ModelAttribute("results") SearchResult results, 
											  @ModelAttribute("search") Search search, 
											  @RequestParam String query, @RequestParam int pageNumber, 
											  @RequestParam String sortBy, @RequestParam String sortOrder, 
											  Model model)
	{
		//SearchResult results = new SearchResult();
		logger.debug(" debug - Getting Good Reads Data...");
		search.setTitle(query);
		results.setSortBy(sortBy);
		results.setSortOrder(sortOrder);
		
		String json =  bookService.getBookListJSON(query, pageNumber, results);
		model.addAttribute("results", results);
		return json;
	}
	
	@RequestMapping(value = "numberOfPages", method = RequestMethod.GET)
	public @ResponseBody String getNumberOfPages(@RequestParam String query)
	{
		SearchResult results = new SearchResult();
		
		String json =  bookService.getBookListJSON(query, 1, results);
		logger.debug(" debug - Getting TotalNumberOfPages ( "+results.getTotalNumberOfPages()+")");
		logger.debug(" debug - Getting Total # of Books ( "+results.getTotalResults()+")");
		
		return Integer.toString(results.getTotalNumberOfPages());
	}
	
}

