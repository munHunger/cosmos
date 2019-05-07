const fs = require("fs");
const { request } = require("graphql-request");

// Custom promisify
function promisify(fn) {
  /**
   * @param {...Any} params The params to pass into *fn*
   * @return {Promise<Any|Any[]>}
   */
  return function promisified(...params) {
    return new Promise((resolve, reject) =>
      fn(
        ...params.concat([
          (err, ...args) =>
            err ? reject(err) : resolve(args.length < 2 ? args[0] : args)
        ])
      )
    );
  };
}

const readdirAsync = promisify(fs.readdir);

function scrapeMovies(rootDirectory, tmdb, callback) {
  return readdirAsync(rootDirectory).then(file =>
    file.forEach(file => {
      request(
        `http://${tmdb.ip}:${tmdb.port}/graphql`,
        `
      query{
        search(query: "${file}"){
          id
          title
          release(format: "year")
        }
      }`
      )
        .then(data => data.search[0])
        .then(data => {
          if (data) callback.apply(this, [data]);
          else console.log(`Could not find movie from ${file}`);
        });
    })
  );
}
module.exports = {
  scrapeMovies
};
