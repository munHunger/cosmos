const fs = require("fs");
var express = require("express");
var graphqlHTTP = require("express-graphql");
var { buildSchema } = require("graphql");

const serviceDiscovery = require("sd");
serviceDiscovery.start(
  "tmdb",
  3341,
  fs.readFileSync("assets/schema.graphql", "utf8")
);

const db = require("./tmdb/db");
const transformer = require("./tmdb/transformer");
const filter = require("./tmdb/filter");

const tmdb = require("./tmdb/apiUtil");

const resolver = (req, param) => {
  return {
    movie: async input => {
      let eq = [((input.filter || {}).id || {}).eq || false]
        .concat(((input.filter || {}).id || {}).in || [])
        .filter(id => id);
      return Promise.all(eq.map(id => tmdb.query(`3/movie/${id}`))).then(
        external =>
          external
            .concat(db.getMovies())
            .filter(m => filter.movieFilter(input, m))
            .map(m => transformer.transform(m))
      );
    },
    search: input =>
      tmdb
        .query(
          `3/search/movie?query=${input.query}${
            input.year ? `&year=${input.year}` : ""
          }`
        )
        .then(data =>
          data.results
            .filter(m => filter.movieFilter(input, m))
            .map(m => transformer.transform(m))
        )
  };
};

var server;
function startServer(port) {
  const app = express();
  app.use(
    "/graphql",
    graphqlHTTP(async (req, res, graphQLParams) => ({
      schema: loadSchema(),
      rootValue: await resolver(req, graphQLParams),
      graphiql: true
    }))
  );
  server = app.listen(port);
  serviceDiscovery.start("tmdb", port);
  console.log(`TMDB server up and running on localhost:${port}/graphql`);

  require("./tmdb/bootstrap").bootstrap();
}

function stopServer() {
  server.close();
}

function loadSchema() {
  return buildSchema(fs.readFileSync("assets/schema.graphql", "utf8"));
}

module.exports = { startServer, stopServer };
