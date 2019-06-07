const chai = require("chai");
const assert = chai.assert;
let underTest = require("./db");

describe("db", () => {
  it("can add data", () =>
    assert(underTest.addMovie({ title: "Matrix" }) > 0, "did not add data"));
  describe("has movie", () => {
    before(() => {
      underTest.addMovie({ title: "gatsby" });
    });
    it("can fetch stored data", () => {
        console.log(underTest.addMovie({}));
      assert(underTest.getMovies().length > 0);
    });
  });
});
