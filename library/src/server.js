const fs = require("fs");
var express = require("express");
var graphqlHTTP = require("express-graphql");
var { buildSchema } = require("graphql");

const port = 3342;
const sdClient = require("sd").start("library", port);

const tmdb = undefined;
sdClient.waitFor("tmdb", config => (this.tmdb = config));

let data = [];
const server = () => {
  return {
    movies: () => data,
    addToWishlist: input => {
      data.push({
        id: input.id
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
    graphqlHTTP({
      schema: loadSchema(),
      rootValue: server(),
      graphiql: true
    })
  );
  app.listen(port);
  console.log(`Library server up and running on localhost:${port}/graphql`);
}

function loadSchema() {
  return buildSchema(fs.readFileSync("assets/schema.graphql", "utf8"));
}
