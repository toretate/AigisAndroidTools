// css
require('bootstrap/dist/css/bootstrap.css');

var $ = require('jquery');
global.jQuery = $;
require('bootstrap');
const dropTable = require('./collection_drop.json');
const rewardTable = require('./collection_reward.json');
const additionalRewardTable = require('./collection_additional_reward.json');

class CollectionParts {

	static load() {
		const collection = BrowserUtil.loadParameters();
		return collection;
	}

}

class BrowserUtil {
	static IsAndroid() {
		var agent = navigator.userAgent;	// navigator:global
		if( agent.search(/Android/) != -1 ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * パラメータの初期化を行う。必要があればシステムからデータを取ってくる
	 */
	static loadParameters() {
		var collection = {};
		if( BrowserUtil.IsAndroid() ) {
			collection.current = wbAPI.loadCurrent();
			collection.target = wbAPI.loadTarget();
			collection.max = wbAPI.loadMax();
		} else {
			collection.current = window.localStorage["CollectionCurrent"];
			collection.target = window.localStorage["CollectionTarget"];
			collection.max = window.localStorage["CollectionMax"];
			if( collection.current == null )collection.current = 200;
			if( collection.target == null )	collection.target = 1300;
			if( collection.max == null )	collection.max = 1500;
		}
		return collection;
	}
}

$(function(){

	var collection_current;	// 収集個数
	var collection_target;	// 収集目標
	var collection_max;		// 今回の収集MAX(もらえる最大個数)

	// 固定時間
	var start = new Date( 2017, 7, 31, 15, 0, 0, 0, 0 );	// 15時開始とする : Monthは0始まりなのに気を付ける
	var last = new Date( 2017, 8, 14, 10, 0, 0, 0, 0 );	// 10時終了 : Monthは0始まりなのに気を付ける

	// Modalの状態保持
	var $modal_open_from;

	/**
	 * パラメータの初期化を行う。必要があればシステムからデータを取ってくる
	 */
	function loadParameters() {
		const collection = CollectionParts.load();
		collection_current = collection.current;
		collection_target  = collection.terget;
		collection_max = collection.max;
		console.log( collection );

		// ゲージの初期化
		showRemainTimeAndParts();
		reloadReward();
	}

	function saveParameters() {
		if( BrowserUtil.IsAndroid() ) {
			wbAPI.save(
					collection_current
					, collection_target
					, collection_max
				);
		} else {
			window.localStorage["CollectionCurrent"] = collection_current;
			window.localStorage["CollectionTarget"] = collection_target;
			window.localStorage["CollectionMax"] = collection_max;
		}
	}

	function showRemainTimeAndParts() {
		// 時間
		var current = new Date();
		if( last.getTime() <= last.getTime() ) current = last;

		showRemainTime( current );
		showParts( current );
	}

	function showRemainTime( current ) {
		if( last <= current ) {
			$("#remainTime").text( "終了しました" );
		} else {
			var dDay = last.getDate() - current.getDate();

			var cHour = current.getHours();
			var dHour = cHour < 10 ? last.getHours() - cHour : last.getHours() + 24 - cHour;
			dHour -= 1; // 現時刻が 0M 0s 0ms なわけないので -1 しておく

			$("#remainTime").text( "あと" +dDay +"日" +dHour +"時間" );
		}
	}

	function showParts( current ) {
		// 収集個数
		var collection_remain = collection_target - collection_current;
		$("#remainParts").text( "あと" +collection_remain +"個" );

		var percent;
		// 目標のゲージ位置
		percent = Math.round( collection_target / collection_target * 100 );
		$("#collection_bar_target").css( {'width': percent + '%'} );
		$("#collection_bar_target").text( collection_target );

		// 目標までのペース位置
		{
			var target_par_hour = collection_target / ( 14 * 24 ); // targetを14日間の時間数で割った個数
			var dHourTime = Math.round( ( current.getTime() - start.getTime() ) / 3600000 );	// 経過時間(H)

			var target_pase_current = Math.round( target_par_hour * dHourTime );	// target/h のとき、現時刻の個数

			percent = Math.round( target_pase_current / collection_target * 100 );
			$("#collection_bar_pase").css( {'width': percent +'%'} );
			$("#collection_bar_pase").text( target_pase_current + " (" +target_par_hour.toFixed(1) +" / 時)" );
		}

		// 現在のゲージ位置
		{
			percent = Math.round( collection_current / collection_target * 100 );
			$("#collection_bar_current").css( {'width': percent +'%'} );
			$("#collection_bar_current").text( collection_current + " (" + percent + "%)" );
		}

		// 現在のペースで何個になるか
		{
			var dHourTime = Math.round( ( current.getTime() - start.getTime() ) / 3600000 );	// 経過時間(H)
			var current_par_hour = collection_current / ( dHourTime ); // currentを現在までの経過時間数で割った個数

			var current_pase_max = Math.floor( current_par_hour * 14 * 24 );	// 14日 x 24時間
			$("#collection_current_pase_max").text( current_pase_max );
		}
	}

	/** メイン報酬のロード */
	function reloadReward() {

		function writeRewardTable( $tbody, data, col_num ) {
			console.log( data );
			var delimiter = Math.ceil( data.parts.length / col_num );

			var header_unit = "<th>個数</th><th>報酬</th>";
			var header = "<tr>";
			for( var i = 0; i< col_num; i++ ) header += header_unit;
			header += "</tr>";
			$tbody.append( header );

			for( var i = 0 ; i < delimiter; i++ ) {

				var str = "<tr>";
				for( var j = 0; j < col_num; j++ ) {
					var part = data.parts[i + delimiter *j];
					var part_class = part[0] <= collection_current ? 'class="less"' : "";
					str += "<td " +part_class +">" +part[0] +"</td><td " +part_class +">" +part[1] +"</td>";
				}
				str += "</tr>"

				$tbody.append( str );
			}
		}

		// Drop
		{
			var data = dropTable;
			var $tbody = $( "#collapseDrop > table > tbody" );
			$tbody.empty();

			var col_num = 4;

			// header
			var header = "<tr><th>名前</th><th>ｶﾘ</th><th>ｽﾀ</th><th>ﾄﾞﾛｯﾌﾟ</th></tr>";
			$tbody.append( header );

			// cell
			for( var i = 0 ; i < data.drops.length; i++ ) {
				var drop = data.drops[i];

				var s1 = "<td>" +drop[0] +"</td>";
				var s2 = "<td>" +drop[1] +"</td>";
				var s3 = "<td>" +drop[2] +"</td>";
				var s4 = "<td>" +drop[3] +"</td>";

				$tbody.append( "<tr>" + s1 + s2 + s3 + s4 + "</tr>" );
			}
		}

		// メイン報酬
		{
			var data = rewardTable;
			var $tbody = $( "#collapseReward > table > tbody" );
			$tbody.empty();
			writeRewardTable($tbody, data, /*col_num*/2);
		}


		// 追加報酬
		{
			var data = additionalRewardTable;
			var $tbody = $( "#collapseAppendReward > table > tbody" );
			$tbody.empty();

			writeRewardTable( $tbody, data, /*col_num*/4 );
		}
	}

	/** イベント処理 */
	$(window).on('load', loadParameters() );

	$( "#collection_edit_target" ).on( 'click', function() {
		$modal_open_from = $( "#collection_edit_target" ).get(0);
		$( '#number-dialog-input' ).val( collection_target );
		$( '#number-modal-dialog' ).modal('show');
	});
	$( "#collection_edit_current" ).on( 'click', function() {
		$modal_open_from = $( "#collection_edit_current" ).get(0);
		$( '#number-dialog-input' ).val( collection_current );
		$( '#number-modal-dialog' ).modal('show');
	});
	$( '#number-modal-dialog' ).on( 'shown.bs.modal', function() { $( '#number-dialog-input').focus(); } );
	$( '#number-modal-dialog' ).on( 'click', '.modal-footer .btn-primary', function() {
		$( '#number-modal-dialog' ).modal( 'hide' );
		var value = $( '#number-dialog-input' ).val();
		if( $modal_open_from == $('#collection_edit_target').get(0) ) {
			collection_target = value;
		} else {
			collection_current = value;
		}
		saveParameters();
		showRemainTimeAndParts();
	});

	// Collapseの開閉による変更
	$('.collapse').on('show.bs.collapse', function() {
		var v1 = $(this).parent().find("span:first");
		v1.attr('class', 'glyphicon glyphicon-triangle-bottom');
	});
	$('.collapse').on('hide.bs.collapse', function() {
		var v1 = $(this).parent().find("span:first");
		v1.attr('class', 'glyphicon glyphicon-triangle-right');
	});

});


