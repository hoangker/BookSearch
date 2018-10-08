package com.tin.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"id","image_url","type"})
public class Book {

	private String title;
	
	private Author author;
	
	private String small_image_url;
	
	private String authorname;
	
	public String getAuthorname() {
		return authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getSmall_image_url() {
		return small_image_url;
	}

	public void setSmall_image_url(String small_image_url) {
		this.small_image_url = small_image_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Book() {}

	
}
