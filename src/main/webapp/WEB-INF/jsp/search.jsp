<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
  
<!DOCTYPE html>
<html>
<head>
 <meta charset="utf-8">
<title>Search for a book</title>
<script type="text/javascript" src="jquery-3.3.1.js"></script>
<script type="text/javascript" src="jquery.simplePagination.js"></script>
<link type="text/css" rel="stylesheet" href="simplePagination.css"/>

<script type="text/javascript">
var searchResults=null;
$(document).keypress(function(event){
    if (event.keyCode === 13) {
    	
        search();
        
        return false;
    }
});

function renderSearchResults()
{
	if(searchResults != null)
	{
		var html="<table>";
		for(i in searchResults)
		{	
 			html+="<tr>"; 

 			html+="<td><img src='"+searchResults[i].small_image_url+"'/></td>";
			
 			var name = searchResults[i].author.name;
			
 			html+="<td>"+searchResults[i].title+"<br/>"+name+"</td>";
			
 			html+="</tr>"; 	
		}
		
		html+="</table>";
		
		$("#mySearchResults").html(html);
	}
	else
	{
		$("#mySearchResults").html("Search returned 0 results. Please try a different search.");
	}
}	
var activeSort = "title";
var titleSortAsc = true;
function sortTitle(){
	//alert("title sorting called");
	if(searchResults == null)
		{
		alert("nothing to sort");
		}
	else
	{
		activeSort = "title";
	
		$("#sortAuthor").html("Author");
		$("#sortAuthorOrder").html("");
		authorSortAsc = true;
		
		if(titleSortAsc)
		{
	
			$("#sortTitleOrder").html("ASC");
			searchResults = sortByKeyASC(searchResults, "title");
			renderSearchResults();
		}
			
		else
			{
	
			$("#sortTitleOrder").html("DESC");
			searchResults = sortByKeyDESC(searchResults, "title");
			renderSearchResults();
			}
			
		
		//alert("title sort "+sort);
		titleSortAsc = !titleSortAsc;
	}

}


var authorSortAsc = true;
function sortAuthor(){
	//alert("author sorting called");
	if(searchResults == null)
		{
		alert("nothing to sort");
		}
	else
	{
		//alert("1");
		activeSort = "author";
		
		$("#sortTitle").html("Title");
		$("#sortTitleOrder").html("");
		titleSortAsc = true;
		
		if(authorSortAsc)
		{
			//alert("2");
			$("#sortAuthorOrder").html("ASC");
			
			searchResults = sortByKeyASC(searchResults, "authorname");
			renderSearchResults();
		}
			
		else
			{
			//alert("3");
			$("#sortAuthorOrder").html("DESC");
			searchResults = sortByKeyDESC(searchResults, "authorname");
			renderSearchResults();
			}
			
		
		//alert("title sort "+sort);
		authorSortAsc = !authorSortAsc;
	}
	
}

function sortByKeyASC(array, key) {
	//alert("sortASC by "+key);
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    });
}
function sortByKeyDESC(array, key) {
	//alert("sortDESC by "+key);
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((y < x) ? -1 : ((y > x) ? 1 : 0));
    });
}
	
function getSortType(){
	
	var sortAuthorOrder = $("#sortAuthorOrder").html();
   	var sortTitleOrder = $("#sortTitleOrder").html();
   	if(sortAuthorOrder != ""){
   		currentSortOrder = sortAuthorOrder;
   		currentSort = "authorname";
   	}
}

function search(){
	var currentSort = "title"
   	var currentSortOrder = "ASC";
   	var sortAuthorOrder = $("#sortAuthorOrder").html();
   	var sortTitleOrder = $("#sortTitleOrder").html();
   	if(sortAuthorOrder != ""){
   		currentSortOrder = sortAuthorOrder;
   		currentSort = "authorname";
   	}
       	
	var val = "";
    val = $("#bookTitle").val();
     
    $.ajax({
    	type: "POST",
    	//url: "http://localhost:8080/BookSearch/results.html?query="+val+"&page=1",
    	//url: '<spring:url value="results.html?query='+val+'&page=1"/>',
    	url: '<spring:url value="results.html?query='+val+'&pageNumber=1&sortBy=title&sortOrder=ASC"/>',
    	dataType: "text",
    	success: function(data){
    		
    		searchResults = JSON.parse(data);
    		//console.log(searchResults);	
    		sortByKeyASC(searchResults, "title");
    		$("#sortTitleOrder").html("ASC")
    		renderSearchResults();
    		
    		
    	},
    	complete: function(){
    		
    		var numberOfPages;
    		$.ajax({
            	type: "GET",
            	url: '<spring:url value="numberOfPages.html?query='+val+'"/>',
            	dataType: "text",
            	success: function(data){
            		var numberOfPages = data;
            		//alert(${results.totalNumberOfPages});
            		$("#pager").pagination({
            	        pages: numberOfPages,
            	        itemsOnPage: 20,
            	        cssStyle: 'light-theme',
            	        onPageClick: function(pageNumber, event){
            	        	
            	        	var currentSort = ""
            	        	var currentSortOrder = "ASC";
            	        	var sortAuthorOrder = $("#sortAuthorOrder").html();
            	        	var sortTitleOrder = $("#sortTitleOrder").html();
            	        	if(sortAuthorOrder != ""){
            	        		currentSortOrder = sortAuthorOrder;
            	        		currentSort = "authorname";
            	        	}
            	        		
            	        	else{
            	        		currentSortOrder = sortTitleOrder;
            	        		currentSort = "title";
            	        	}
            	        	
            	        	
            	        	//alert("sort type before reload:"+currentSort);
            	        	//alert("sort order before reload:"+currentSortOrder);
            	        	var val = "";
            				val = $("#bookTitle").val();
            				
            	        	$.ajax({
            	            	type: "POST",
            	            	url: '<spring:url value="results.html?query='+val+'&pageNumber='+pageNumber+'&sortBy='+currentSort+'&sortOrder='+currentSortOrder+'"/>',
            	            	dataType: "text",
            	            	success: function(data){
            	            		
            	            		if(data.length > 0)
            	            		{
            	            			searchResults = JSON.parse(data);
            	                		console.log(searchResults);	
            	            			
            	            		}	
            	            		else {
            	            			searchResults = null;
            	            		}
            	            		
            	            		if(currentSort == "title")
            	            			{
            	            				if(currentSortOrder == "ASC"){
            	            					sortByKeyASC(searchResults, "title");
            	            					//alert("sorting title asc");
            	            				}
            	            					
            	            				else{
            	            					sortByKeyDESC(searchResults, "title");
            	            					//alert("sorting title desc");
            	            				}
            	            					
            	            			}
            	            		else
            	            			{
            		            			if(currentSortOrder == "ASC")
            		        					sortByKeyASC(searchResults, "authorname");
            		        				else
            		        					sortByKeyDESC(searchResults, "authorname");
            	            			
            	            			}
            	            		renderSearchResults(); 
            	            		
            	            		//scroll to the top	
            	           		    $([document.documentElement, document.body]).animate({
            	           		        scrollTop: $("#top").offset().top
            	           		    }, 100);
            	            	}
            	            });
            	        }
            	    });            		


            		
            		
            		
            	}
    		});
    		
    		
    		//close complete function
    	}
    	
    });
       
}
	
	
</script>

</head>
<body id="top">
<h1>Good Reads API (very minimal)</h1>
<form:form modelAttribute="search">
	<table>
		<tr>
			<td>
			Search for a book:
			</td>
			<td><form:input path="title" id="bookTitle" /></td>
			<td>
<!-- 			<input type="submit" value="Search"/> -->
				<input id="searchBTN" type="button" value="Search" onClick="javascript:search();"/>
			</td>
		</tr>
		<tr>
			<td colspan="3">
			Sort: <a href="#" onclick='javascript:sortTitle();'><span id="sortTitle">Title</span> <span id="sortTitleOrder"></span></a> | <a href="#"  onclick="javascript:sortAuthor();"><span id="sortAuthor">Author</span> <span id="sortAuthorOrder"></span></a>
			</td>
			
		</tr>
	</table>
</form:form>
<div id="mySearchResults">
</div>
<div id="pager">
</div>
</body>
</html>