const api = require("./apiUtil");
const db = require("./db");

module.exports = {
  bootstrap: () => {
    api
      .query("3/movie/popular")
      .then(latest => latest.results.forEach(movie => db.addMovie(movie)));
  }
};
