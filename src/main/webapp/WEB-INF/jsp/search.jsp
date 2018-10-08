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
<script type="text/javascript">

	$(document).ready(function(){
		/*
		$.getJSON('http://localhost:8080/BookSearch/', {
			ajax ; 'true'
			
		}, function(data){
			var html = '<li>'
			
		});*/
	
	});
	
	
	var searchResults=null;
	function search(){
		var val = "";
        val = $("#book").val();
        //alert(val);
        $.ajax({
        	type: "POST",
        	//url: "http://localhost:8080/BookSearch/results.html?query="+val+"&page=1",
        	url: '<spring:url value="results.html?query='+val+'&page=1"/>',
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
        		renderSearchResults(); 
        		
        	}
        	
        });
        
	}
	
	function renderSearchResults()
	{
		
		if(searchResults != null)
		{
			
			var html ="<a href=# onclick='javascript:prev();'>Prev</a> | <a href=# onclick='javascript:next();'>Next</a>"
	    		html+="<table>";
			for(i in searchResults)
			{
	 				
	 			html+="<tr>"; 
					html+="<td><img src='"+searchResults[i].small_image_url+"'/></td>";
					var name = searchResults[i].author.name;
					
					html+="<td>"+searchResults[i].title+"<br/>"+name+"</td>";
					//html+="<td>"+searchResults.GoodreadsResponse.search.results.work[j].best_book.author.name+"</td>";
					html+="</tr>"; 	
					
				
			}
				
			
			html+="</table>";
			html+="<a href=# onclick='javascript:prev();'>Prev</a> | <a href=# onclick='javascript:next();'>Next</a>"
			$("#results").html(html);
		}
		else
		{
			$("#results").html("Search returned 0 results. Please try a different search.");
		}
		
		
	}
	var titleSortAsc = true;
	function sortTitle(){
		
		if(searchResults == null)
			alert("nothing to sort");
		else{
			var sort="";
			if(titleSortAsc)
			{
				sort="ASC";
				searchResults = sortByKeyASC(searchResults, "title");
				renderSearchResults();
			}
				
			else
				{
				sort = "DESC";
				searchResults = sortByKeyDESC(searchResults, "title");
				renderSearchResults();
				}
				
			
			//alert("title sort "+sort);
			titleSortAsc = !titleSortAsc;
		}
	
	}
	
	var authorSortAsc = true;
	function sortAuthor(){
		if(searchResults == null)
			alert("nothing to sort");
		else
			{
			var sort="";
			if(authorSortAsc)
				sort="ASC";
			else
				sort = "DESC";
			alert("author sort");
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
<body>
<h1>Good Reads API (very minimal)</h1>
<form:form modelAttribute="search">
	<table>
		<tr>
			<td>
			Search for a book:
			</td>
			<td><form:input path="title"/></td>
			<td>
				<input type="submit" value="Search"/>
			</td>
		</tr>
		
	</table>
</form:form>

</body>
</html>