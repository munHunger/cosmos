const transform = movie => {
  return {
    ...movie,
    release: input =>
      input.format === "year" ? movie.release.substring(0, 4) : movie.release,
    poster: input =>
      `https://image.tmdb.org/t/p/w${input.width ? input.width : 500}${
        movie.poster
      }`
  };
};
module.exports = {
  transform
};
