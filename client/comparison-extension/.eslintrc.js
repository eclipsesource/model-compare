/** @type {import('eslint').Linter.Config} */
module.exports = {
    extends: '@eclipse-glsp',
    parserOptions: {
        tsconfigRootDir: __dirname,
        project: 'tsconfig.json'
    },
    rules: {
        'no-undef': 'off',
        'brace-style': 'off'
    }
};
