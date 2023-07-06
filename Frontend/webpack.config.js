const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');


module.exports = {
  resolve: {
    fallback: {
      crypto: require.resolve('crypto-browserify'),
      buffer: require.resolve("buffer"),
      stream: require.resolve("stream-browserify")
    },
  },
  optimization: {
    usedExports: true
  },
  entry: {
    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js'),
    homePage: path.resolve(__dirname, 'src', 'pages', 'homePage.js'),
    animePage: path.resolve(__dirname, 'src', 'pages', 'animePage.js'),
    searchPage: path.resolve(__dirname, 'src', 'pages', 'searchPage.js'),
    loginPage: path.resolve(__dirname, 'src', 'pages', 'loginPage.js'),
    updatePasswordPage: path.resolve(__dirname, 'src', 'pages', 'updatePasswordPage.js'),
    updateLoginPage: path.resolve(__dirname, 'src', 'pages', 'updateLoginPage.js'),
    landingSlideshow: path.resolve(__dirname, 'src', 'styles', 'landingSlideshow.js')
  },

  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,

    /*    openPage: 'http://localhost:8080',
        disableHostCheck: true,*/
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
      template: './src/signup.html',
      filename: 'signup.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/login.html',
      filename: 'login.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/updatePassword.html',
      filename: 'updatePassword.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/updateLogin.html',
      filename: 'updateLogin.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/homepage.html',
      filename: 'homepage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/animePage.html',
      filename: 'animePage.html',
      inject: false
    }),

    new HtmlWebpackPlugin({
      template: './src/searchPage.html',
      filename: 'searchPage.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/landingPage.html',
      filename: 'landingPage.html',
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
    new CleanWebpackPlugin(),

  ]

}

