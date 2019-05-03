const ip = require("ip");
const BroadcastChannel = require("broadcast-channel");

/**
 * A presented client in the cosmos network
 * @typedef {Object} Config
 * @property {String} name the announced name of a service
 * @property {number} port the port that the service listens to
 * @property {String} ip the ip of the client on the local network
 */

/**
 * @callback Query
 * @param {String} name the announced name to search for
 * @returns {Config} the config of the queried object or undefined if it is not in the network
 */

/**
 * @callback Ping
 */

/**
 * @callback Poll
 */

/**
 * @callback Clear
 */

/**
 * @callback WaitForCallback
 * @param {Config} config the queried service configuration
 */
/**
 * @callback WaitFor
 * @param {String} name the name to search the network for
 * @param {WaitForCallback} callback the function to call when found
 */

/**
 * The service discovery client
 * @typedef {Object} Client
 * @property {Config} config the config of the local client that has been announced to the network
 * @property {Ping} ping Ping the network with your info
 * @property {Query} query Search for a registered service
 * @property {Poll} poll Request an update from the entire network.
 *      This should force all clients to reping.
 * @property {Clear} clear Clear the local list of services
 *      Note that this means forgetting about all other services.
 *      Current service will still be visible to others
 * @property {WaitFor} waitFor Waits for a service to be available in the network
 */

module.exports = {
  /**
   * registers a client into the network.
   * @param {String} name the name to register into the cosmos network
   * @param {number} port the port that the client is listening to
   * @returns {Client}
   */
  start: (name, port) => {
    let client = {
      config: { name, port, ip: ip.address() },
      channel: undefined,
      ping: () => client.channel.postMessage(client.config),
      /**
       * @type {Config[]}
       */
      others: [],
      query: name => client.others.find(client => client.name === name),
      poll: () => client.channel.postMessage({ action: "poll" }),
      clear: () => (client.others = []),
      listeners: {},
      waitFor: (name, callback) => {
        if (!client.query(name)) client.listeners[name] = callback;
        else callback.apply(this, [client.query(name)]);
      }
    };
    client.channel = new BroadcastChannel("cosmos");
    client.channel.onmessage = msg => {
      if (msg) {
        if (msg.action === "poll") {
          console.log("Heartbeat recieved");
          client.ping();
        } else if (msg.name && msg.ip && msg.port) {
          console.log(`Service ${msg.name} joined the network`);
          let old = client.others.find(c => c.name === msg.name);
          if (old) {
            console.log(
              `Service already registered at ${JSON.stringify(
                old
              )}. overriding with ${JSON.stringify(msg)}`
            );
            Object.assign(old, msg);
          } else {
            client.others.push(msg);
            if (client.listeners[msg.name])
              client.listeners[msg.name].apply(this, [msg]);
          }
        }
      }
    };
    client.ping();
    client.poll();
    console.log(`Registering service ${name} to network`);

    return client;
  }
};
