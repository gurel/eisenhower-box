'use strict';

var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var CopyWebpackPlugin = require('copy-webpack-plugin');

var assetsPath = 'app/assets/';
var srcPath = path.join(__dirname, assetsPath);
var outputPath = path.join(__dirname, 'public/dist');
var bootstrapPath = path.join(__dirname, 'node_modules/bootstrap/dist');

var config = {
	devtool: 'source-map',
  target: 'web',
  entry: {
    app: path.join(srcPath, 'index.js'),
    vendor: ['angular', 'angular-route']
  },
  resolve: {
    alias: {},
    root: srcPath,
    extensions: ['', '.js'],
    modulesDirectories: ['node_modules', assetsPath]
  },
  output: {
    path: outputPath,
    publicPath: '',
    filename: '[name].js',
    pathInfo: true
  },

  module: {
    noParse: [],
    loaders: [
      {
        test: /\.js?$/,
        exclude: /node_modules/,
        loader: 'babel'
      }, {
        test: /(\.global)|(\.min)\.css$/,
        loader: ExtractTextPlugin.extract('style-loader','css-loader')
      }, {
        test: /^((?!(\.global|\.min)).)*\.css$/,
        loader: ExtractTextPlugin.extract(
          'style-loader',
          'css-loader?modules&importLoaders=1&localIdentName=[name]__[local]___[hash:base64:5]'
        )
      }, {
        test: /\.(png|jpg|jpeg|gif|svg|woff|woff2|ttf|eot)$/,
          loader: 'file'
      },  {
        test: /\.html$/,
        loader: 'raw'
      }
    ]
  },
  plugins: [
    new webpack.optimize.CommonsChunkPlugin('vendor', 'vendor.js'),
    // new webpack.optimize.UglifyJsPlugin({
    //     compress: { warnings: false },
    //     output: { comments: false }
    // }),
    new ExtractTextPlugin("styles.css", { allChunks: true }),
    new webpack.NoErrorsPlugin(),
    new CopyWebpackPlugin([
      {from: path.join(bootstrapPath, 'css', 'bootstrap.min.css'), to: 'bootstrap.min.css', force: true},
      {from: path.join(bootstrapPath, 'fonts', 'glyphicons-halflings-regular.ttf'), to: '../fonts/glyphicons-halflings-regular.ttf', force: true},
      {from: path.join(bootstrapPath, 'fonts', 'glyphicons-halflings-regular.woff'), to: '../fonts/glyphicons-halflings-regular.woff', force: true},
      {from: path.join(bootstrapPath, 'fonts', 'glyphicons-halflings-regular.woff2'), to: '../fonts/glyphicons-halflings-regular.woff2', force: true},
      {from: path.join(bootstrapPath, 'fonts', 'glyphicons-halflings-regular.eot'), to: '../fonts/glyphicons-halflings-regular.eot', force: true}
    ])
  ]
};

module.exports = config;
