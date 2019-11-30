const { makeExecutableSchema } = require("graphql-tools");
const resolvers = require("./resolvers");
const fs = require("fs");

const typeDefs = ["schema"]
  .map(f => `assets/${f}.graphql`)
  .map(f => fs.readFileSync(f, "utf8"));
const executableSchema = makeExecutableSchema({
  typeDefs: typeDefs,
  resolvers
});

module.exports = { executableSchema, typeDefs };
