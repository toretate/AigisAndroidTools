const webpack = require('webpack');
const path = require('path');

// releaseオプションを付けないとデバッグビルド
const DEBUG = process.argv.indexOf('--release') === -1;

module.exports = {
    entry: {
        bundle: './collection.js'
    }
    , output: {
        path: path.join(__dirname, 'public'),
        // filename: '[name].js'
        filename: 'bundle.js'
    }
    , module: {
        loaders: [
            {
                loader: 'babel-loader',
                exclude: /node_modules/,
                test: /\.js[x]?$/,
                query: {
                    cacheDirectory: true,
                    presets: ['es2015']
                }
            },
            { test: /\.css$/, loader: 'style-loader!css-loader' },
            { test: /\.svg$/, loader: 'url-loader?mimetype=image/svg+xml' },
            { test: /\.woff$/, loader: 'url-loader?mimetype=application/font-woff' },
            { test: /\.woff2$/, loader: 'url-loader?mimetype=application/font-woff' },
            { test: /\.eot$/, loader: 'url-loader?mimetype=application/font-woff' },
            { test: /\.ttf$/, loader: 'url-loader?mimetype=application/font-woff' },
            { test: /\.json$/, loader: 'json-loader' }
        ]
    }
    , devtool: DEBUG ? 'inline-source-map' : false
};