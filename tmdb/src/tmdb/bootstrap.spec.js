const chai = require("chai");
const assert = chai.assert;
const sinon = require("sinon");

const api = require("./apiUtil");
const db = require("./db");
const bootstrap = require("./bootstrap");

describe("bootstrap", () => {
  let apiStub;
  let dbStub;
  before(() => {
    apiStub = sinon.stub(api, "query");
    apiStub.returns(new Promise(resolve => resolve([1, 3])));

    dbStub = sinon.stub(db, "addMovie").returns(null);
  });
  it("queries popular movies", () => {
    assert(apiStub.called, "did not call api");
  });
});
