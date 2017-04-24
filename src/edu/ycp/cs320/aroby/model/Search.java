package edu.ycp.cs320.aroby.model;

public class Search {
	private String search;
	private String extraSearch;
	
	public Search(){
		
	}
	
	public void setSearch(String search){
		this.search = search;
	}
	
	public String getSearch(){
		return search;
	}

	public String getExtraSearch() {
		return extraSearch;
	}

	public void setExtraSearch(String extraSearch) {
		this.extraSearch = extraSearch;
	}
}
