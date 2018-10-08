package com.tin.service;



import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tin.model.Book;
import com.tin.model.SearchResult;
import com.tin.model.Work;

@Service("bookService")
public class BookServiceImpl implements BookService {

	private static final String API_KEY = "RDfV4oPehM6jNhxfNQzzQ";
	public static final int MAX_GOODREADS_PAGES = 100;
	public static final int MAX_BOOKS_PER_PAGE = 20;
	
	private static final String ARRAY = "array";
	
	/* (non-Javadoc)
	 * @see com.tin.service.BookService#findAllBooks()
	 */
	/*public String findAllBooks(String bookString) {
		
		
		System.out.println("findbooks:"+bookString);
		String json = "";
		//
		try {
			String urlString = "https://www.goodreads.com/search.xml?key="+API_KEY+"&q="+URLEncoder.encode(bookString, "UTF-8")+"";
			System.out.println(urlString);
			URL obj = new URL(urlString);	
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			//con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = con.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				
				JSONObject xmlJSONObj = XML.toJSONObject(response.toString());
				// print result
				json = xmlJSONObj.toString(4);
				
			} else {
				System.out.println("GET request not worked");
			}
		}
		catch(Exception e)
		{
			return "";
		}
		
		return json;
		
	}*/
	
	public String getBookListJSON(String searchString, int page, SearchResult searchResult)
	{
		byte[] bookData = {};
		try
		{
		
			getBookList(searchString, page, searchResult);
			List<Book> books = searchResult.getBooks();
			if(searchResult != null && searchResult.getBooks() != null && searchResult.getBooks().size() > 0)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(out,  books);
				
				bookData = out.toByteArray();
			}
			
		}
		catch(Exception e)
		{
			return "";
		}
		return new String(bookData);
	}
	
	public void getBookList(String searchString, int page, SearchResult searchResult)  {
		
		
		System.out.println("findbooks:"+searchString);
		String json = "";
		List<Book> books = new ArrayList<Book>();
		byte[] bookData;
		int maxPages = 0;
		//
		try {
			
			JSONObject goodReadsResponse = getGoodReadsData(searchString, page);
			JSONObject search = goodReadsResponse.getJSONObject("search");
			int totalBookResults = search.getInt("total-results");
			if(totalBookResults == 0)
				return;
			JSONObject results = search.getJSONObject("results");				
			
			int numberOfPages = 0;
			
			//1. read total-results
			if(totalBookResults > 0)
				numberOfPages = (int)Math.ceil(totalBookResults/MAX_BOOKS_PER_PAGE);
			
			if(numberOfPages >= MAX_GOODREADS_PAGES)
				maxPages = MAX_GOODREADS_PAGES;
			else
				maxPages = numberOfPages;
			
			searchResult.setTotalNumberOfPages(maxPages);
			
			//System.out.println(results.toString(4));
			JSONArray workArray = results.getJSONArray("work");
			//JSONObject workArray = results.getJSONObject("work");
			//System.out.println(workArray.toString(4));
			//2. loop to maxPages and create the book list
			ObjectMapper mapper = new ObjectMapper();
			/*String toParse = "  [  { \"best_book\": { \"title\": \"The End of Harry Potter\", "
													+ " \"author\": {\"name\": \"JK Rowling\"} } },  "
								+ "{ \"best_book\": { \"title\": \"Potter\", "
													+ " \"author\": {\"name\": \"Tin Hoang\"} } } ] "; */
			
					
			Work[] work = mapper.readValue(workArray.toString(4), Work[].class);
			for(Work w: work) {
				Book b = w.getBest_book();
				//so we can sort in javascript easily
				b.setAuthorname(b.getAuthor().getName());
				books.add(w.getBest_book());
			}
			
			searchResult.setBook(books);
			/*ByteArrayOutputStream out = new ByteArrayOutputStream();
			mapper = new ObjectMapper();
			mapper.writeValue(out,  book);
			
			bookData = out.toByteArray();*/
			
		}
		catch(Exception e)
		{
			//log error
			
		}
		
	}
	
	private JSONObject getGoodReadsData(String query, int page)
	{
		JSONObject goodReadsResponse = null;
		try {
			String urlString = "https://www.goodreads.com/search.xml?key="+API_KEY+"&q="+URLEncoder.encode(query, "UTF-8")+"&page="+page;
			System.out.println(urlString);
			URL obj = new URL(urlString);	
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			//con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = con.getResponseCode();
			//System.out.println("GET Response Code :: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
	
				JSONObject xmlJSONObj = XML.toJSONObject(response.toString());
				goodReadsResponse = xmlJSONObj.getJSONObject("GoodreadsResponse");
				
				
			} else {
				System.out.println("GET request not worked");
			}
		}
		catch(Exception e)
		{
			return goodReadsResponse;
		}
		
		return goodReadsResponse;
		
	}
	
}
