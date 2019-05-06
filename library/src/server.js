var { mergeTypes } = require("merge-graphql-schemas");

const fs = require("fs");
var express = require("express");
var graphqlHTTP = require("express-graphql");
var { buildSchema } = require("graphql");
const graphqlHelper = require("graphql-helper");

const movieScraper = require("./scraper/movieScraper");

const port = 3342;
const sdClient = require("sd").start("library", port);

let data = [];

let services = undefined;
sdClient.waitFor("tmdb", config => {
  services = { tmdb: config };
  movieScraper.scrapeMovies("../movies", config, movie =>
    data.push({ id: movie.id, status: "IN_LIBRARY" })
  );
});

const server = (req, param) => {
  return {
    movie: async () => {
      let res = await graphqlHelper.compositeQuery(
        "movie",
        param.query,
        services,
        data,
        data => data
      );
      return res;
    },
    addToWishlist: input => {
      data.push({
        id: input.id,
        status: "WISHLIST"
      });
      return "OK";
    }
  };
};

startServer(port);

function startServer(port) {
  var app = express();
  app.use(
    "/graphql",
    graphqlHTTP(async (req, res, graphQLParams) => ({
      schema: loadSchema(),
      rootValue: await server(req, graphQLParams),
      graphiql: true
    }))
  );
  app.listen(port);
  console.log(`Library server up and running on localhost:${port}/graphql`);
}

function loadSchema() {
  return buildSchema(
    mergeTypes(
      [
        fs.readFileSync("assets/schema.graphql", "utf8"),
        fs.readFileSync("assets/other.graphql", "utf8")
      ],
      { all: true }
    )
  );
}
