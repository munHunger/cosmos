# Git repo for the Cosmos system.

This repo is intended for a micro-service based Movie/TV management system. Each service including the frontend web-interface has its own folder in the root of the repo.

## Starting up

For all services you need to run

```
npm install
```

in the directory of the service

First thing to start is the tmdb gateway with

```
npm run dev
```

On initial startup it will ask for a tmdb token, which can be found on the tmdn dev portal.
Its' config will be written to disk at `~/.config/cosmos/tmdb.json`

After tmdb, the order doesn't really matter, so you can start `library`, `streamer`, and `client/api` in any order.
They should all find eachother via service discovery.
Once connected to eachother they can share schemas that may or may not be stiched together.

To get anything streaming a folder should be created at the root of this project named `movies`

In there you can add any movie and it should be found by cosmos.
I recomend downloading the blender open movies as a test. ex `/movies/alike/Alike short film.mov`
