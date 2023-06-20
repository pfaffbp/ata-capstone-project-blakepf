const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js'),
    LoginSignupPage: path.resolve(__dirname, 'src', 'pages', 'LoginSignupPage.js'),
    homePage: path.resolve(__dirname, 'src', 'pages', 'homePage.js'),
    animePage: path.resolve(__dirname, 'src', 'pages', 'animePage.js'),
    searchPage: path.resolve(__dirname, 'src', 'pages', 'searchPage.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    proxy: [
      {
        context: [
          '/',
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/LoginSignup.html',
      filename: 'LoginSignup.html',
      inject: false
    }),
      template: './src/homepage.html',
      filename: 'homepage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/animepage.html',
      filename: 'animepage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/searchPage.html',
      filename: 'searchPage.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
