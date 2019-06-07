const fs = require("fs");
const { startServer } = require("./server");
const port = 3341;

const configPath = process.env.HOME + "/.config/cosmos/tmdb.json";

if (!fs.existsSync(configPath)) {
  const readline = require("readline");

  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
  });

  let def = JSON.parse(fs.readFileSync("assets/tmdb.json", "utf8"));

  console.log("Missing configuration for TMDB");
  console.log("=> API Authentication");
  rl.question("v3 key: ", v3 => {
    def.auth.v3 = v3;
    rl.question("v4 key: ", v4 => {
      def.auth.v4 = v4;
      fs.mkdirSync(configPath.substring(0, configPath.lastIndexOf("/")), {
        recursive: true
      });
      fs.writeFileSync(configPath, JSON.stringify(def, null, 2));
      rl.close();
      console.log(`wrote config to ${configPath}`);
      startServer(port);
    });
  });
} else startServer(port);
