# Java-Pirate-Bay-Api
Java based library that uses Jsoup to scrape data from The Piratebay

Requires the JSoup dependency to work in other projects.

<h2>Usage</h2>
<b>This gets the top 100 torrents category</b>
<br>
ArrayList<Torrent> torrents = new ArrayList<Torrent>();
<br>
torrents = Jpa.Search(new Query(TorrentCategory.Top100));

<b>There are 5 overloaded query constructors</b>
<ol>
<li> For the 48h categories only Query(TorrentCategory constant) e.g. new Query(TorrentCategory.Games48h);
<li> Query(string query, int page) e.g. new Query("fallout 4", 1);
<li> Query(string query, int page, int category) e.g. new Query("james brown", 1, TorrentCategory.FLAC);
<li> Query(string query, int page, QueryOrder order) e.g. new Query("the walking dead", 2, QueryOrder.ByName);
<li> Query(string query, int page, int category, QueryOrder order) e.g. new Query("frozen", 1, TorrentCategory.HDMovies, QueryOrder.ByDefault);
</ol>
