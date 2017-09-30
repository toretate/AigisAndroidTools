// css : bootstrap
require('bootstrap/dist/css/bootstrap.css');

// css : Flat UI
require('flat-ui/css/flat-ui.css');

// js : jQuery
// var $ = require('jquery');
// global.jQuery = $;

// // js : bootstrap
// require('bootstrap');

// js : Flat UI
$ = require('flat-ui/js/jquery-2.0.3.min');
global.jQuery = $;
require('flat-ui/js/jquery-ui-1.10.3.custom.min');
require('flat-ui/js/jquery.ui.touch-punch.min');
require('flat-ui/js/bootstrap.min');
require('flat-ui/js/bootstrap-select');
require('flat-ui/js/bootstrap-switch');
require('flat-ui/js/flatui-checkbox');
require('flat-ui/js/flatui-radio');
require('flat-ui/js/application');

// 経験値テーブル
const expTable = require('./exp_table.json');

import route from 'riot-route'
riot.route = route;
import './tags/compose/compose-table-root.tag'
const tags = riot.mount('*');
riot.route.start( true );

$(function(){

	var use_sariet = false;
	/**
	 * パラメータの初期化を行う。必要があればシステムからデータを取ってくる
	 */
	function loadParameters() {
		// if( isAndroid() ) {
		// 	collection_current = wbAPI.loadCurrent();
		// 	collection_target = wbAPI.loadTarget();
		// 	collection_max = wbAPI.loadMax();
		// } else {
			// use_sariet = window.localStorage["UseSariet"];
			// if( use_sariet == null ) use_sariet = false;
		// }
	}

	function saveParameters() {
		// if( isAndroid() ) {
		// 	wbAPI.save(
		// 			collection_current
		// 			, collection_target
		// 			, collection_max
		// 		);
		// } else {
			// window.localStorage["UseSariet"] = use_sariet;
		// }
	}

	$('.collapse').on('show.bs.collapse', function() {
		var v1 = $(this).parent().find("span");
		v1.attr('class', 'glyphicon glyphicon-triangle-bottom');
	});
	$('.collapse').on('hide.bs.collapse', function() {
		var v1 = $(this).parent().find("span");
		v1.attr('class', 'glyphicon glyphicon-triangle-right');
	});
});
