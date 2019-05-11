const fs = require("fs");
const express = require("express");

const port = 3343;
const sdClient = require("sd").start("streamer", port);
const { request } = require("graphql-request");

var app = express();
app.get("/video", function(req, res) {
  console.log(`query{
  movie(filter: [{field: "id", eq:"${req.query.id}"}]){
    location {
      folder
      video
    }
  }
}`);
  request(
    `http://${sdClient.query("library").ip}:${
      sdClient.query("library").port
    }/graphql`,
    `
query{
  movie(filter: [{field: "id", eq:"${req.query.id}"}]){
    location {
      folder
      video
    }
  }
}`
  )
    .then(data => data.movie[0].location)
    .then(data => {
      const path = data.folder + "/" + data.video;
      const stat = fs.statSync(path);
      const fileSize = stat.size;
      const range = req.headers.range;
      if (range) {
        const parts = range.replace(/bytes=/, "").split("-");
        const start = parseInt(parts[0], 10);
        const end = parts[1] ? parseInt(parts[1], 10) : fileSize - 1;
        const chunksize = end - start + 1;
        const file = fs.createReadStream(path, { start, end });
        const head = {
          "Content-Range": `bytes ${start}-${end}/${fileSize}`,
          "Accept-Ranges": "bytes",
          "Content-Length": chunksize,
          "Content-Type": "video/mp4"
        };
        res.writeHead(206, head);
        file.pipe(res);
      } else {
        const head = {
          "Content-Length": fileSize,
          "Content-Type": "video/mp4"
        };
        res.writeHead(200, head);
        fs.createReadStream(path).pipe(res);
      }
    });
});

app.listen(port);
