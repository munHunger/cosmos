package jpa;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jpa.Query;
import jpa.Torrent;

public class Jpa {

	public static ArrayList<Torrent> Search (Query query) throws IOException {		
		ArrayList<Torrent> result = new ArrayList<Torrent>();
		
			Jsoup.connect(Constants.Url);
		
			Document doc = Jsoup.connect(query.TranslateToUrl())
					.userAgent(Constants.UserAgent)
					.timeout(5000)
					.get();
			
			// get all table rows
			Elements tableRows = doc.getElementsByTag("tr");
			
			for (Element element : tableRows) {
				
				if (!element.hasClass("header")) {
					final Torrent torrent = new Torrent();
					
					// first td, lets get the categories
					Element td1 = element.children().select("td").first();
					
					ArrayList<Element> categories = td1.children().select("a");
					torrent.CategoryParent = categories.get(0).text();
					torrent.Category = categories.get(1).text();
					
					// second td, get some more info
					// get the torrent name
					Element td2 = element.children().select("td").get(1);
					Element aTorrentName = td2.children().select("a").first();
					torrent.Name = aTorrentName.text();
					
					// get the file link
					torrent.Link = aTorrentName.attr("href");
					
					// get the magnet
					Element torrentMagnet = td2.children().select("a").get(1);
					torrent.Magnet = torrentMagnet.attr("href");
					
					// get vip and trusted info
					ArrayList<Element> icons = td2.children().select("img");
					String attribute = new String();
					
					for (Element icon : icons) {
						// search all the icons for the alt attribute
						attribute = icon.attr("alt");
						
						if (attribute.equals("VIP")) {
							torrent.VIP = true;
						}
						if (attribute.equals("Trusted")) {
							torrent.Trusted = true;
						}
					}
					
					// get date, size, and uploader
					Element details = td2.select("font").first();
					String torrentInfo = details.text();
					String[] splitInfo = torrentInfo.split(",");
					
					torrent.Uploaded = splitInfo[0].substring(9);
					torrent.Size = splitInfo[1].substring(6);
					torrent.Uled = splitInfo[2].substring(9);
					
					// third td, get the seeds
					Element td3 = element.children().select("td").get(2);
					torrent.Seeds = Integer.parseInt(td3.text());
					
					// forth td, get the leechers
					try {
						Element td4 = element.children().select("td").get(3);
						torrent.Leechers = Integer.parseInt(td4.text());
					}
					catch (Exception e) {
						//e.printStackTrace();
					}
					
					result.add(torrent);
				}
			}	
		
		// setup the threadpool
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
		
		for (Torrent file : result) {
			if (file.CategoryParent.equals("Video")) {
				Runnable worker = new ImageSearch(file);
				executorService.execute(worker);
			}
		}
		
		// all torrents added to the threadpool
		executorService.shutdown();
		
		// block until all the threads are finished
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return result;
	}	
}
