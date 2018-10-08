<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>    
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
    
<!DOCTYPE html>
<html>
<head>
 <meta charset="utf-8">
<title>Search Results</title>
<script type="text/javascript" src="jquery-3.3.1.js"></script>
<script type="text/javascript" src="jquery.simplePagination.js"></script>
<link type="text/css" rel="stylesheet" href="simplePagination.css"/>


<script type="text/javascript">
var searchResults=null;
$(document).ready(function(){
	
	$("#pager").pagination({
        pages:${results.totalNumberOfPages},
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
        	
        	$.ajax({
            	type: "POST",
            	url: '<spring:url value="results.html?query=${search.title}&pageNumber='+pageNumber+'&sortBy='+currentSort+'&sortOrder='+currentSortOrder+'"/>',
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
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
    });
}
function sortByKeyDESC(array, key) {
    return array.sort(function(a, b) {
        var x = a[key]; var y = b[key];
        return ((y < x) ? -1 : ((y > x) ? 1 : 0));
    });
}
</script>

</head>
<body id="top">
<h1>Good Reads API (very minimal)</h1> 
<div>
<p>
<a href='<spring:url value="search.html"/>'>Start New Search</a> Sort: <a href="#" onclick='javascript:sortTitle();'><span id="sortTitle">Title</span> <span id="sortTitleOrder"></span></a> | <a href="#"  onclick="javascript:sortAuthor();"><span id="sortAuthor">Author</span> <span id="sortAuthorOrder"></span></a> 
</p>
 
</div>

<form:form modelAttribute="results">
	
	<div id="mySearchResults">
		<table>
			
				<c:forEach var="book" items="${results.books}">
					
					<tr>
						<td><img src="${book.small_image_url}"></img></td>
						<td>${book.title}<br/>${book.author.name}</td>
					</tr>
				</c:forEach>
				
		</table>
	</div>
	
	<div id="pager"/>
		
</form:form>
</body>
</html>