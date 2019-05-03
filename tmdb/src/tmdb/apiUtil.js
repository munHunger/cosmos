const axios = require("axios");

const query = (url) => {
    axios.get("https://api.themoviedb.org" + url)
}