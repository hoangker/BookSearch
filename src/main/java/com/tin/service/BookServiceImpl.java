package com.tin.service;



import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tin.controller.SearchController;
import com.tin.model.Book;
import com.tin.model.SearchResult;
import com.tin.model.Work;

@Service("bookService")
public class BookServiceImpl implements BookService {

	private final Logger logger = Logger.getLogger(BookServiceImpl.class);
	//IN PRODUCTION ENV store key in environment variable.
	private static final String API_KEY = "RDfV4oPehM6jNhxfNQzzQ";
	public static final int MAX_GOODREADS_PAGES = 100;
	public static final int MAX_BOOKS_PER_PAGE = 20;
	
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
		List<Book> books = new ArrayList<Book>();
		int maxPages = 0;
		try {
			
			JSONObject goodReadsResponse = getGoodReadsData(searchString, page);
			JSONObject search = goodReadsResponse.getJSONObject("search");
			int totalBookResults = search.getInt("total-results");
			searchResult.setTotalResults(totalBookResults);
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

			JSONArray workArray = results.getJSONArray("work");
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
			
		}
		catch(Exception e)
		{
			//log error
			logger.fatal(" fatal error - exception: "+ e.getMessage());
		}
		
	}
	
	private JSONObject getGoodReadsData(String query, int page)
	{
		JSONObject goodReadsResponse = null;
		try {
			
			String urlString = "https://www.goodreads.com/search.xml?key="+API_KEY+"&q="+URLEncoder.encode(query, "UTF-8")+"&page="+page;
			logger.debug(" debug - urlString: " + urlString);
			URL obj = new URL(urlString);	
			
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			logger.debug(" debug - Sending GoodReads Request");
			//con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode = con.getResponseCode();
			logger.debug(" debug - Getting GoodReads Response");
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
				logger.debug(" debug - Successfully retrieved GoodreadsReponse.");
				
			} else {
				//System.out.println("GET request not worked");
				logger.error(" debug - Get Request did not work!");
			}
		}
		catch(Exception e)
		{
			logger.fatal(" fatal error - exception: "+ e.getMessage());
			return goodReadsResponse;
		}
		
		return goodReadsResponse;
		
	}
	
}
