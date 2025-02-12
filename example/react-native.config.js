const path = require('path');

const pkg = require('../package.json');

module.exports = {
  dependencies: {
    [pkg.name]: {
      root: path.join(__dirname, '..'),
      platforms: {
        android: {
          cmakeListsPath: path.join(__dirname, '..', 'android', 'CMakeLists.txt'),
        },
        ios: null,
      },
    },
  },
};
