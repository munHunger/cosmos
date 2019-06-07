const getPort = require("get-port");
const serviceDiscovery = require("sd");
const { startServer, stopServer } = require("./server");
const chai = require("chai");
const assert = chai.assert;
const sinon = require("sinon");
const url = `http://localhost:3341`;
const request = require("supertest")(url);

describe("GraphQL", () => {
  var sdStart;
  before(async () => {
    sdStart = sinon.stub(serviceDiscovery, "start").returns(null);
    startServer(await getPort());
  });
  after(() => stopServer());
  it("Registers to the SD network", () =>
    assert(sdStart.called, "service discovery wasn't called"));
  it("Can search for gatsby", done => {
    request
      .post(`/graphql`)
      .send({
        query: `query{
            search(query: "gatsby"){
              title
              poster(width: 500)
              release(format: "year")
            }
          }`
      })
      .expect(200)
      .end((err, res) => {
        if (err) return done(err);
        let search = res.body.data.search[0];
        assert(
          search.title === "The Great Gatsby",
          "Title is not 'The Great Gatsby'"
        );
        assert(
          search.poster.indexOf("500" > -1),
          "Poster does not have a width of 500"
        );
        assert(
          search.release.match(/[0-9]{4}/g),
          "The year release format is not 4 digits"
        );
        done();
      });
  });
  it("Can get a list of popular movies", done => {
    request
      .post(`/graphql`)
      .send({
        query: `query{
          movie{
            title
          }
        }`
      })
      .expect(200)
      .end((err, res) => {
        if (err) return done(err);
        assert(res.body.data.movie.length > 0, "List of movies is empty");
        done();
      });
  });
  it("Can search for a specific ID", done => {
    request
      .post(`/graphql`)
      .send({
        query: `query{
          movie(filter: {id: { eq: 5000}}){
            id
          }
        }`
      })
      .expect(200)
      .end((err, res) => {
        if (err) return done(err);
        assert(
          res.body.data.movie.length === 1,
          "List of movies did not return a single element"
        );
        assert(res.body.data.movie[0].id === 5000, "Wrong ID was returned");
        done();
      });
  });
});
