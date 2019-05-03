var express = require("express");
var graphqlHTTP = require("express-graphql");
var { buildSchema } = require("graphql");

var schema = buildSchema(`
  type Query {
      movie: [Movie]
  }

  type Movie {
    id: Int,
    title: String,
    tagline: String,
    overview: String,
    budget: Int,
    genre: [String]
  }
  type Rating {
      average: Int,
      count: Int
  }
`);

let data = [
  {
    id: 0,
    title: "Star wars",
    tagline: "wars in space",
    overview: "pissed of brats everywhere",
    budget: 9999,
    genre: ["action"],
    rating: {
      average: 10,
      count: 100
    }
  }
];

// The root provides a resolver function for each API endpoint
async function root() {
  return {
    movie: () => data
  };
}

var app = express();
app.use(
  "/graphql",
  graphqlHTTP(async (request, response, graphQLParams) => ({
    schema: schema,
    rootValue: await root(),
    graphiql: true
  }))
);
app.listen(4000);
console.log("Running a GraphQL API server at localhost:4000/graphql");
