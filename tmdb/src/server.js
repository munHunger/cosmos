const fs = require("fs");
var express = require("express");
var graphqlHTTP = require("express-graphql");
var { buildSchema } = require("graphql");

const serviceDiscovery = require("sd");
serviceDiscovery.start("tmdb", 3341);

const db = require("./tmdb/db");

const server = () => {
  return {
    movie: () => db.getMovies()
  };
};

const configPath = process.env.HOME + "/.config/cosmos/tmdb.json";
let config = {};
if (!fs.existsSync(configPath)) {
  const readline = require("readline");

  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
  });

  let def = JSON.parse(fs.readFileSync("assets/tmdb.json", "utf8"));

  console.log("Missing configuration for TMDB");
  console.log("=> API Authentication");
  rl.question("v3 key: ", v3 => {
    def.auth.v3 = v3;
    rl.question("v4 key: ", v4 => {
      def.auth.v4 = v4;
      fs.mkdirSync(configPath.substring(0, configPath.lastIndexOf("/")), {
        recursive: true
      });
      fs.writeFileSync(configPath, JSON.stringify(def, null, 2));
      rl.close();
      console.log(`wrote config to ${configPath}`);
      startServer(3341);
    });
  });
} else startServer(3341);

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
  console.log(`TMDB server up and running on localhost:${port}/graphql`);

  require("./tmdb/bootstrap").bootstrap();
}

function loadSchema() {
  return buildSchema(fs.readFileSync("assets/schema.graphql", "utf8"));
}
