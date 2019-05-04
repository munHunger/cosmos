const serviceDiscovery = require("sd").start("client", 4000);

const { request } = require("graphql-request");

const query = `
query{
  movie{
    id
    title
    tagline
    overview
    budget
    genre
  }
}`;

serviceDiscovery.waitFor("tmdb", tmdb => {
  var express = require("express");

  var path = require("path");
  var bodyParser = require("body-parser");
  var app = express();

  app.use(bodyParser.json());
  app.use(bodyParser.urlencoded({ extended: false }));
  app.use(express.static(path.join(__dirname, "public")));

  app.get("/api/popular", (req, res) => {
    request(`http://${tmdb.ip}:${tmdb.port}/graphql`, query).then(data =>
      res.status(200).json(data)
    );
  });
  /* GET home page. */
  app.get("/", function(req, res, next) {
    //Path to your main file
    res
      .status(200)
      .sendFile(path.join(__dirname + "../../../app/dist/cosmos/index.html"));
  });
  app.get("*", (req, res, next) => {
    res
      .status(200)
      .sendFile(path.join(__dirname + "../../../app/dist/cosmos/" + req.url));
  });

  app.listen(8080);
});
