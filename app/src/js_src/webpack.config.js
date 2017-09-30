const webpack = require('webpack');
const path = require('path');

// releaseオプションを付けないとデバッグビルド
const DEBUG = process.argv.indexOf('--release') === -1;

module.exports = {
    entry: {
        collection:'./collection.js',
        compose:'./compose.js'
    }
    , output: {
        path: path.join(__dirname, '../main/assets'),
        // filename: '[name].js'
        filename: '[name].bundle.js'
    }
    , module: {
        loaders: [
            {
                test: /\.tag$/,
                exclude: /node_modules/,
                loader: 'riotjs-loader',
                query: { type: 'none' },
                enforce: 'pre'
            },
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
            { test: /\.png$/, loader: 'url-loader' },
            { test: /\.json$/, loader: 'json-loader' }
        ]
    }, plugins: [
        new webpack.ProvidePlugin({
            riot: 'riot'
        })
    ]
    , devtool: 'inline-source-map'
};