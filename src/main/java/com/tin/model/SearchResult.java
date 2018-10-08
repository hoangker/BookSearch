package com.tin.model;


import java.util.List;

public class SearchResult {

	private int totalNumberOfPages;
	private Search search;
	private String sortBy;
	private int totalResults;
	
	public int getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	private String sortOrder;
	
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	public int getTotalNumberOfPages() {
		return totalNumberOfPages;
	}
	public void setTotalNumberOfPages(int totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBook(List<Book> books) {
		this.books = books;
	}
	private int currentPage;
	private List<Book> books;
	
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
