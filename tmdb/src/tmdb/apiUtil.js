const axios = require("axios");
const fs = require("fs");
const auth = JSON.parse(
  fs.readFileSync(process.env.HOME + "/.config/cosmos/tmdb.json", "utf8")
).auth;

const genres = JSON.parse(fs.readFileSync("assets/genres.json", "utf8"));

const query = url => {
  return axios
    .get(
      `https://api.themoviedb.org/${url}${
        url.indexOf("?") > -1 ? "&" : "?"
      }api_key=${auth.v3}`,
      { proxy: false }
    )
    .then(res => {
      if (res.status === 200) {
        if (res.data.results)
          res.data.results = res.data.results.map(movie => movieMapper(movie));
        else res.data = movieMapper(res.data);
        return res.data;
      }
    });
};

const movieMapper = tmdbMovie => {
  return {
    id: tmdbMovie.id,
    title: tmdbMovie.title,
    tagline: tmdbMovie.tagline,
    overview: tmdbMovie.overview,
    backdrop: tmdbMovie.backdrop_path,
    poster: tmdbMovie.poster_path,
    budget: tmdbMovie.budget,
    rating: {
      average: Math.floor(tmdbMovie.vote_average * 10),
      count: tmdbMovie.vote_count
    },
    release: tmdbMovie.release_date,
    genre: (tmdbMovie.genre_ids || tmdbMovie.genres)
      .map(g => genres.find(g2 => g2.id === (g.id || g)))
      .map(g => g.name)
  };
};

module.exports = {
  query
};
