let movies = [];

module.exports = {
  addMovie: movie => movies.push(movie),
  getMovies: () => movies
};
