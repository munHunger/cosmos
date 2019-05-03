const axios = require("axios");
const fs = require("fs");
const auth = JSON.parse(
  fs.readFileSync(process.env.HOME + "/.config/cosmos/tmdb.json", "utf8")
).auth;

const query = url => {
  return axios
    .get(
      `https://api.themoviedb.org/${url}${
        url.indexOf("?") > -1 ? "&" : "?"
      }api_key=${auth.v3}`
    )
    .then(res => {
      if (res.status === 200) return res.data;
    });
};

module.exports = {
  query
};
