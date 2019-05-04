const fs = require("fs");
var express = require("express");
var graphqlHTTP = require("express-graphql");
var { buildSchema } = require("graphql");
const { request } = require("graphql-request");

const port = 3342;
const sdClient = require("sd").start("library", port);

let tmdb = undefined;
sdClient.waitFor("tmdb", config => (tmdb = config));

let data = [];
const server = () => {
  return {
    movies: async () => {
      let res = await request(
        `http://${tmdb.ip}:${tmdb.port}/graphql`,
        `
query{
  movie(filter: {
    id:{
      in:[${data.map(d => d.id).join(",")}]
    }
  }){
    id
    title
    overview
    poster
    rating {
      average
    }
    release(format: "year")
    genre
  }
}`
      ).then(data => data.movie);
      console.log(res);
      return res;
    },
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
