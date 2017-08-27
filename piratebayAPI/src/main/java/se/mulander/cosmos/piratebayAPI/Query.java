package se.mulander.cosmos.piratebayAPI;

import java.net.URL;
import java.net.URLEncoder;

public class Query {

    public QueryOrder Order;
    public int Category;
    public int Page;
    public String Term;
    public boolean Mode48h;
    
    // constructors
    // used for the 48hr and top100 categories
    public Query(String term) {
    	Term = term;
    	Mode48h = true;
    }
    
    public Query(int category) {
    	Term = "";
    	this.Category = category;
    	this.Page = 0;
    	Order = QueryOrder.ByDefault;
    	Mode48h = false;
    }
    
    public Query(String term, int page) {
    	Term = term;
    	Page = page;
    	Category = TorrentCategory.All;
    	Order = QueryOrder.ByDefault;
    	Mode48h = false;
    }
    
    public Query(String term, int page, int category) {
    	Term = term;
    	Page = page;
    	Category = category;
    	Order = QueryOrder.ByDefault;
    	Mode48h = false;
    }
    
    public Query(String term, int page, QueryOrder order) {
    	Term = term;
    	Page = page;
    	Category = TorrentCategory.All;
    	Order = order;
    	Mode48h = false;
    }
    
    public Query(String term, int page, int category, QueryOrder order) {
    	Term = term;
    	Page = page;
    	Category = category;
    	Order = order;
    	Mode48h = false;
    }
    
    public String TranslateToUrl() {
    	String url = new String();
    	
    	if (!Mode48h) {
			try
			{
				url = Constants.Url +
						"/search/" +
						URLEncoder.encode(Term, "UTF-8") + "/" +
						Integer.toString(Page) + "/" +
						Integer.toString(Order.getValue()) + "/" +
						Integer.toString(Category);
			}
			catch(Exception e)
			{
				return null;
			}
		}
    	else {
			url = Constants.Url +
    				"/top/" +
    				Term;
		}
		return url;
    }
}
