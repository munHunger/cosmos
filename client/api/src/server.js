const serviceDiscovery = require("sd").start("client", 8080);

const { request } = require("graphql-request");

serviceDiscovery.waitFor("tmdb", tmdb => {
  serviceDiscovery.waitFor("library", library => {
    var express = require("express");

    var path = require("path");
    var bodyParser = require("body-parser");
    var app = express();

    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(express.static(path.join(__dirname, "public")));

    app.get("/api/popular", (req, res) => {
      request(
        `http://${tmdb.ip}:${tmdb.port}/graphql`,
        `
      query{
        movie{
          id
          title
          poster
          rating {
            average
          }
          release(format: "year")
          genre
        }
      }`
      ).then(data => res.status(200).json(data));
    });
    app.get("/api/library", (req, res) =>
      request(
        `http://${library.ip}:${library.port}/graphql`,
        `
      query{
        movie{
          id
          status
          title
          poster
          release(format: "year")
          genre
        }
      }`
      ).then(data => res.status(200).json(data))
    );
    app.post("/api/wish", (req, res) => {
      request(
        `http://${library.ip}:${library.port}/graphql`,
        `mutation{
        addToWishlist(id: ${req.body.id})
      }`
      ).then(data => res.status(200).json(data));
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
});
