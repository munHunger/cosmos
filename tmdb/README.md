# TMDB

This is a gateway API towards tmdb.
All services that goes towards tmdb should pass through this service to avoid having register authentication on multiple locations

All data fetched should be stored in mem.

Do note that this service will ask for token authentication upon first startup (tested on linux, milage may vary)
