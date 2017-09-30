<!-- opts.type で種別が入ってくる -->
<compose-table>
	<div class="panel panel-default">
		<div class="panel-heading" role="tab">
			<h4 class="panel-title">
				<a data-toggle="collapse" href="#collapse{ opts.type.type }"><span class="glyphicon glyphicon-triangle-right" />{ opts.type.name }</a>
			</h4>
		</div>

		<div id="collapse{ opts.type.type }" class="panel-collapse collapse" role="tabpanel">
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

		baseExp = 8000;
		useSariet = false;
		remainTable = [];

		this.on('update', () => {
			this.baseExp = opts.baseexp;
			if( this.useSariet ) {
				this.baseExp = this.baseExp * 1.1;
			}

			var maxLvNormal = Math.min( opts.type.table.length, 50 );
			var maxLvCC = opts.type.table.length;
			switch( opts.type.type ) {
				case 'iron':
				case 'copper':
					maxLvCC = -1;
					break;
				case 'silver':
					maxLvCC = 55;
					break;
			}

			this.remainTable = this.getRemainTable( opts.type.table, this.baseExp, maxLvNormal );
			if( maxLvCC !== -1 ) {
				this.remainTableCC = this.getRemainTable( opts.type.table, this.baseExp, maxLvCC );
			}
		});


		this.getRemainTable = ( table, addExp, maxLv ) => {
			var remain = table[ table.length -1 ];
			var maxLv = maxLv;
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
			if( exp === remain ) return { lv:index +1, remain:0, exp:table[index] };
			index -= 1;
			return { lv:index, remain:table[index +1] - remain, exp:table[index] };
		} 
	</script>

	<!--
			   0,   32,   65,  100,  135,  172,  211,  250,  291,  333,
			 418,  506,  598,  693,  791,  893,  998, 1106, 1218, 1333,
			1503, 1679, 1862, 2052, 2249, 2452, 2662, 2879, 3103, 3333 
	-->

</compose-table>