import './compose-table.tag'
const expTable = require('../../exp_table.json');

<compose-table-root>
	<div class="panel panel-body">
		<div class="col-md-1 panel panel-default">
			<ul class="nav nav-pills nav-stacked panel-body">
				<li class="active"><a onclick="{ typeClicked.bind(this, 'black') }">黒</a></li>
				<li><a onclick="{ typeClicked.bind(this, 'plutinum') }">白</a></li>
				<li><a onclick="{ typeClicked.bind(this, 'gold') }">金</a></li>
				<li><a onclick="{ typeClicked.bind(this, 'silver') }">銀</a></li>
				<li><a onclick="{ typeClicked.bind(this, 'copper') }">銅</a></li>
				<li><a onclick="{ typeClicked.bind(this, 'iron') }">鉄</a></li>
			</ul>
		</div>
		<div class="col-md-11">
			<compose-table if="{ this.type === 'gold' }" type="{ expTable.gold }" baseexp="{ opts.baseexp }" />
			<compose-table if="{ this.type === 'silver' }" type="{ expTable.silver }" baseexp="{ opts.baseexp }" />
			<compose-table if="{ this.type === 'copper' }" type="{ expTable.copper }" baseexp="{ opts.baseexp }" />
			<compose-table if="{ this.type === 'iron' }" type="{ expTable.iron }" baseexp="{ opts.baseexp }" />
		</div>
	</div>


<script>

	this.type = undefined;

	this.on('update', () => {
		this.expTable = expTable;
	});

	this.typeClicked = ( type, e ) => {
		this.type = type;
		this.update();

		var $li = $( $(e.srcElement).closest('li')[0] );
		var $ul = $( $li.closest('ul')[0] );
		var $current_li = $( $ul.children('.active')[0] );
		if( $current_li !== undefined ) $current_li.removeClass('active');
		$li.addClass('active');
	}

	riot.route( _ => {
		this.update();
		return;
	} );
</script>

</compose-table-root>