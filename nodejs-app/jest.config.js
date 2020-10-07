module.exports = {
  transform: {
    '^.+\\.ts?$': 'ts-jest'
  },
  testEnvironment: 'node',
  testRegex: '/tests/.*\\.(test|spec)?\\.(ts|tsx|js)$',
  moduleFileExtensions: ['ts', 'tsx', 'js', 'jsx', 'json', 'node'],
  "collectCoverage": true,
  "collectCoverageFrom": [
    "src/**/*.{js,jsx,ts}",
    "resources/**/*.{js,jsx,ts}",
    "!**/node_modules/**",
    "!**/coverage/**"
  ],
  "coverageThreshold": {
    "global": {
      "branches": 20,
      "functions": 20,
      "lines": 20
      //"statements": -10
    }
  }
}





