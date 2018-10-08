package com.tin.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties({"id","books_count","ratings_count", "text_reviews_count","original_publication_year" ,"original_publication_month","original_publication_day","average_rating"})
public class Work {

	private Book best_book;


	public Book getBest_book() {
		return best_book;
	}

	public void setBest_book(Book best_book) {
		this.best_book = best_book;
	}
}
