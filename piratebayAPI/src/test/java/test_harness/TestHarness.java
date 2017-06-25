package test_harness;

import java.io.IOException;
import java.util.ArrayList;

import se.mulander.cosmos.piratebayAPI.Query;
import se.mulander.cosmos.piratebayAPI.Torrent;
import se.mulander.cosmos.piratebayAPI.TorrentCategory;
import se.mulander.cosmos.piratebayAPI.Jpa;

public class TestHarness {

	public static void main(String[] args) {
		ArrayList<Torrent> torrents = new ArrayList<Torrent>();

		try {
			torrents = Jpa.Search(new Query("Rick and morty", 0));
			// search for the total top 100 category
			torrents = Jpa.Search(new Query(TorrentCategory.Top100));
		}	
		catch (Exception e) {
			// the search method can throw an IOException i.e. timeout
			e.printStackTrace();
		}
		
		int resultCount = torrents.size();
		System.out.println(resultCount + " results");
		for (int i = 0; i < resultCount; i++) {
			// iterate through the arraylist and display some info
			System.out.println("Name: " + torrents.get(i).Name);
			System.out.println("Parent category: " + torrents.get(i).CategoryParent);
			System.out.println("Image: " + torrents.get(i).CoverImage);
			System.out.println("VIP: " + torrents.get(i).VIP);
			System.out.println("Trusted: " + torrents.get(i).Trusted);
		}
	}
}