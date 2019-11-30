const { PubSub } = require("apollo-server-express");

const pubsub = new PubSub();

const subscriptionTopics = {};

module.exports = { pubsub, subscriptionTopics };
