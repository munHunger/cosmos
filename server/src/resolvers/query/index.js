const logger = require("../../logger").logger("Query");

const ping = async input => {
  logger.debug("requested ping");
  return "pong";
};

module.exports = { ping };
