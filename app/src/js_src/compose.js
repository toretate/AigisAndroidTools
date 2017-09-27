// css
require('bootstrap/dist/css/bootstrap.css');

var $ = require('jquery');
global.jQuery = $;
require('bootstrap');
const expTable = require('./exp_table.json');

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
			use_sariet = window.localStorage["UseSariet"];
			if( use_sariet == null ) use_sariet = false;
		// }

		// ゲージの初期化
		showRemainTimeAndParts();
		reloadReward();
	}

	function saveParameters() {
		// if( isAndroid() ) {
		// 	wbAPI.save(
		// 			collection_current
		// 			, collection_target
		// 			, collection_max
		// 		);
		// } else {
			window.localStorage["UseSariet"] = use_sariet;
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
