package com.tin.service;


import com.tin.model.SearchResult;

public interface BookService {

	//String findAllBooks(String bookString);
	
	void getBookList(String searchString, int page, SearchResult result);
	
	String getBookListJSON(String searchString, int page, SearchResult result);

}