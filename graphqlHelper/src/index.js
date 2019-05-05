const fs = require("fs");
const { request } = require("graphql-request");
let structure = undefined;

function queryParser(query) {
  return query
    .split("\n")
    .slice(1, -1)
    .slice(1, -1)
    .map(line => ({ name: line.match(/\w+/)[0], value: line }));
}
function merge(other) {
  structure = Object.keys(getStructure())
    .filter(
      key => Object.keys(other).filter(otherKey => otherKey === key).length > 0
    )
    .map(key => {
      let res = structure[key];
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
  return structure;
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

async function resolveOthers(type, query, ids, services) {
  let parameters = queryParser(query)
    .filter(p => getStructure()[type][p.name] !== "this")
    .map(p => p.value)
    .join("\n");
  if (parameters.length > 0)
    return request(
      `http://${services["tmdb"].ip}:${services["tmdb"].port}/graphql`,
      `
query{
${type}(filter: {
id:{
  in:[${ids.join(",")}]
}
}){
id
${parameters}
}
}`
    ).then(data => data.movie);
  return undefined;
}

async function compositeQuery(type, query, services, data, transformer) {
  let res = await resolveOthers(type, query, data.map(d => d.id), services);
  return data.map(data => ({
    ...Object.keys(getStructure()[type])
      .filter(key => getStructure()[type][key] !== "this")
      .map(key => ({
        name: key,
        value: async () => res.find(r => r.id === data.id)[key]
      }))
      .reduce((acc, val) => {
        acc[val.name] = val.value;
        return acc;
      }, {}),
    ...transformer(data)
  }));
}

function load() {
  structure = toJSON(fs.readFileSync("assets/schema.graphql", "utf8"), "this");
  merge(toJSON(fs.readFileSync("assets/other.graphql", "utf8"), "tmdb"));
  return structure;
}

function getStructure() {
  return structure ? structure : load();
}
module.exports = {
  queryParser,
  merge,
  toJSON,
  getStructure,
  resolveOthers,
  compositeQuery
};
