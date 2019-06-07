const { PubSub } = require("graphql-subscriptions");

const pubsub = new PubSub();
var { mergeTypes } = require("merge-graphql-schemas");

const fs = require("fs");
var express = require("express");
var graphqlHTTP = require("express-graphql");
var { buildSchema, execute, subscribe } = require("graphql");
const { SubscriptionServer } = require("subscriptions-transport-ws");
const { createServer } = require("http");
const graphqlHelper = require("graphql-helper");

const movieScraper = require("./scraper/movieScraper");

const port = 3342;
const sdClient = require("sd").start("library", port);

let data = [];

const ADDED_MOVIE_WISHLIST_TOPIC = "newWishList";

let services = undefined;
sdClient.waitFor("tmdb", config => {
  services = { tmdb: config };
  movieScraper.scrapeMovies("../movies", config, (movie, location) =>
    data.push({
      id: movie.id,
      status: "IN_LIBRARY",
      location
    })
  );
});

const server = (req, param) => {
  return {
    movie: async input => {
      let res = await graphqlHelper.compositeQuery(
        "movie",
        param.query,
        services,
        data,
        graphqlHelper.filter.filter,
        input,
        data => data
      );
      return res;
    },
    addToWishlist: input => {
      data.push({
        id: input.id,
        status: "WISHLIST"
      });
      pubsub.publish(ADDED_MOVIE_WISHLIST_TOPIC, input.id);
      return "OK";
    },
    wishlistItemAdded: () => pubsub.asyncIterator(ADDED_MOVIE_WISHLIST_TOPIC)
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
      graphiql: true,
      subscriptionsEndpoint: `ws://localhost:${3241}/subscriptions`
    }))
  );
  app.listen(port);
  console.log(`Library server up and running on localhost:${port}/graphql`);
  const ws = createServer(express());
  ws.listen(3241, () => {
    console.log(`Library subscription websocet alive on port 3241`);
    new SubscriptionServer({
      execute,
      subscribe,
      schema: loadSchema()
    }, {
      server: ws,
      path: '/subscriptions'
    })
  })
}

function loadSchema() {
  return buildSchema(
    mergeTypes(
      [fs.readFileSync("assets/schema.graphql", "utf8")].concat(
        sdClient
          .registeredClients()
          .map(client => client.payload)
          .filter(payload => payload)
      ),
      { all: true }
    )
  );
}
