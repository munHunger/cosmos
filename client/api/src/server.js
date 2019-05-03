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

serviceDiscovery.waitFor("tmdb", tmdb =>
  request(`http://${tmdb.ip}:${tmdb.port}/graphql`, query).then(data =>
    console.log(data)
  )
);
