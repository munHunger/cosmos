var { mergeTypes } = require("merge-graphql-schemas");

const fs = require("fs");
var express = require("express");
var graphqlHTTP = require("express-graphql");
var { buildSchema } = require("graphql");
const { request } = require("graphql-request");

const port = 3342;
const sdClient = require("sd").start("library", port);

let tmdb = undefined;
sdClient.waitFor("tmdb", config => (tmdb = config));

let data = [
  {
    id: 299534,
    status: "WISHLIST"
  }
];
function queryParser(query) {
  console.log(query);
  return query
    .split("\n")
    .slice(1, -1)
    .slice(1, -1)
    .map(line => ({ name: line.match(/\w+/)[0], value: line }));
}
const server = (req, param) => {
  return {
    movie: async () => {
      let structure = merge(
        toJSON(fs.readFileSync("assets/schema.graphql", "utf8"), "this"),
        toJSON(fs.readFileSync("assets/other.graphql", "utf8"), "tmdb")
      ).movie;
      let parameters = queryParser(param.query)
        .filter(p => structure[p.name] !== "this")
        .map(p => p.value)
        .join("\n");
      let res = undefined;
      if (parameters.length > 0)
        res = await request(
          `http://${tmdb.ip}:${tmdb.port}/graphql`,
          `
query{
  movie(filter: {
    id:{
      in:[${data.map(d => d.id).join(",")}]
    }
  }){
    id
    ${parameters}
  }
}`
        ).then(data => data.movie);
      return data.map(data =>
        Object.keys(structure)
          .map(key => ({
            name: key,
            value: async input => {
              if (structure[key] === "this") return data[key];
              else {
                return res.find(r => r.id === data.id)[key];
              }
            }
          }))
          .reduce((acc, val) => {
            acc[val.name] = val.value;
            return acc;
          }, {})
      );
    },
    addToWishlist: input => {
      data.push({
        id: input.id
      });
      return "OK";
    }
  };
};

console.log(toJSON(fs.readFileSync("assets/schema.graphql", "utf8"), "this"));
console.log(
  merge(
    toJSON(fs.readFileSync("assets/schema.graphql", "utf8"), "this"),
    toJSON(fs.readFileSync("assets/other.graphql", "utf8"), "tmdb")
  )
);

function merge(local, other) {
  return Object.keys(local)
    .filter(
      key => Object.keys(other).filter(otherKey => otherKey === key).length > 0
    )
    .map(key => {
      let res = local[key];
      Object.keys(other[key])
        .filter(
          field =>
            Object.keys(res).filter(localKey => localKey === field).length === 0
        )
        .forEach(otherField => (res[otherField] = other[key][otherField]));
      return { name: key, value: res };
    })
    .reduce((acc, val) => {
      acc[val.name.toLowerCase()] = val.value;
      return acc;
    }, {});
}

function toJSON(schema, endpoint) {
  return schema
    .split("\n")
    .map(s => s.trim())
    .reduce((acc, val) => {
      if (val === "}" || val.length == 0) return acc;
      if (val.indexOf("{") > -1) acc.push([val.replace(/\s{/, "")]);
      else acc[acc.length - 1].push(val);
      return acc;
    }, [])
    .filter(list => list.length > 0 && list[0].indexOf("type") == 0)
    .map(list => {
      let name = list[0].replace(/type\s*/, "");
      let res = { name, def: {} };
      list
        .slice(1)
        .map(field => ({
          name: field.substring(
            0,
            field.indexOf("(") > -1 ? field.indexOf("(") : field.indexOf(":")
          ),
          target: endpoint
        }))
        .forEach(field => (res.def[field.name] = field.target));
      return res;
    })
    .reduce((acc, val) => {
      acc[val.name] = val.def;
      return acc;
    }, {});
}

startServer(port);

function startServer(port) {
  var app = express();
  app.use(
    "/graphql",
    graphqlHTTP(async (req, res, graphQLParams) => ({
      schema: loadSchema(),
      rootValue: await server(req, graphQLParams),
      graphiql: true
    }))
  );
  app.listen(port);
  console.log(`Library server up and running on localhost:${port}/graphql`);
}

function loadSchema() {
  let res = buildSchema(fs.readFileSync("assets/schema.graphql", "utf8"));
  let other = buildSchema(fs.readFileSync("assets/other.graphql", "utf8"));
  return buildSchema(
    mergeTypes(
      [
        fs.readFileSync("assets/schema.graphql", "utf8"),
        fs.readFileSync("assets/other.graphql", "utf8")
      ],
      { all: true }
    )
  );
}
