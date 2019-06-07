let movies = [{ title: "gatsby" }];

module.exports = {
  addMovie: movie => {
    console.log("hello");
    return movies.push(movie);
  },
  getMovies: () => {
    console.log("world")
    return movies;
  }
};
