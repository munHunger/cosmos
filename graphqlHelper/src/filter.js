const filter = (input, data) => {
  if (!input.filter) return true;
  return input.filter
    .map(f => {
      return Object.keys(f)
        .filter(f => f !== "field")
        .map(comp => {
          let compVal = f[comp];
          let field = data[f.field];
          if (typeof field === "string") {
            field = field.toUpperCase();
            compVal = compVal.toUpperCase();
          }
          switch (comp) {
            case "eq":
              return field == compVal;
            case "lt":
              return field < compVal;
            case "gt":
              return field > compVal;
            case "in":
              return (
                compVal
                  .map(f => (typeof f === "string" ? f.toUpperCase() : f))
                  .filter(f => field === f).length > 0
              );
            case "contains":
              return field.indexOf(compVal) > -1;
          }
        })
        .reduce((acc, val) => (acc &= val), true);
    })
    .reduce((acc, val) => (acc &= val), true);
};
module.exports = {
  filter
};
