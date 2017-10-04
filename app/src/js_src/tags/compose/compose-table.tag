<!-- opts.type で種別が入ってくる -->
<compose-table>
	<div class="panel panel-default">
		<nav class="navbar navbar-default">
			<div class="navbar-header">
				<span class="navbar-brand">{opts.type.name}</span>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a onclick="{ typeClicked.bind(this, 'before') }">CC前</a></li>
					<li if="{ opts.type['max-cc0'] }"><a onclick="{ typeClicked.bind(this, 'cc0') }">ＣＣ</a></li>
					<li if="{ opts.type['max-cc1'] }"><a onclick="{ typeClicked.bind(this, 'cc1') }">覚１</a></li>
					<li if="{ opts.type['max-cc2'] }"><a onclick="{ typeClicked.bind(this, 'cc2') }">覚２</a></li>
				</ul>
			</div>
		</nav>
		<div class="panel panel-body">
			<table class="table table-condensed table-striped table-bordered">
				<thead>
					<tr><th>Lv</th><th>次Lv</th>
				</thead>

				<tbody>
				<!--
					<tr each="{ i, index in opts.type.table }">
						<td>{ index +1 }</td><td>{ i }</td>
					</tr>
				-->
					<tr each="{ i, index in this.remainTable }">
						<td>{ i.lv }</td><td>{ i.remain }</td><td>{ i.exp }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<script>

		this.baseExp = 8000;
		this.useSariet = false;
		this.remainTable = [];
		this.cctype = 'before';

		this.on('update', () => {
			this.baseExp = opts.baseexp;
			if( this.useSariet ) {
				this.baseExp = this.baseExp * 1.1;
			}

			var maxLv = opts.type.max;			// CC前最大レベル
			var maxLvCC0 = opts.type['max-cc0'];	// CC後最大レベル
			var maxLvCC1 = opts.type['max-cc1'];	// 覚１最大レベル
			var maxLvCC2 = opts.type['max-cc2'];	// 覚２最大レベル
			var max = 0;
			switch( this.cctype ) {
				case 'before':	max = maxLv; break;
				case 'cc0': 	max = maxLvCC0; break;
				case 'cc1': 	max = maxLvCC1; break;
				case 'cc2': 	max = maxLvCC2; break;
			}
			max --;

			this.remainTable = this.getRemainTable( opts.type.table, this.baseExp, max );
		});


		this.getRemainTable = ( table, addExp, maxLv ) => {
			var maxLv = maxLv;
			var remain = table[ maxLv ];
			var lv = maxLv;
			var result = [];
			while( ( 0 <= lv || lv !== undefined ) && 0 <= remain) {
				var current = this.getLv( remain, table );
				current.lv += 1;
				result.push( current );
				lv = current.lv;
				remain -= addExp;
			}

			return result;
		}


		this.getLv = ( remain, table ) => {
			var index = table.findIndex( (exp, index) => {
				return remain <= exp; 
			} );
			if( index === -1 ) return { lv:undefined, remain:undefined, exp:undefined };
			var exp = table[index];
			if( exp === remain ) return { lv:index, remain:0, exp:table[index] };
			index -= 1;
			return { lv:index, remain:table[index +1] - remain, exp:table[index] };
		} 

		this.typeClicked = ( type, e ) => {
			this.cctype = type;
			this.update();

			var $li = $( $(e.srcElement).closest('li')[0] );
			var $ul = $( $li.closest('ul')[0] );
			var $current_li = $( $ul.children('.active')[0] );
			if( $current_li !== undefined ) $current_li.removeClass('active');
			$li.addClass('active');
		}
	</script>

	<!--
			   0,   32,   65,  100,  135,  172,  211,  250,  291,  333,
			 418,  506,  598,  693,  791,  893,  998, 1106, 1218, 1333,
			1503, 1679, 1862, 2052, 2249, 2452, 2662, 2879, 3103, 3333 
	-->

</compose-table>