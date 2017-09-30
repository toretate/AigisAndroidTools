import './compose-table.tag'
const expTable = require('../../exp_table.json');

<compose-table-root>
	<div>
		<compose-table type="{ expTable.silver }" baseexp="{ opts.baseexp }" />
		<compose-table type="{ expTable.copper }" baseexp="{ opts.baseexp }" />
		<compose-table type="{ expTable.iron }" baseexp="{ opts.baseexp }" />
	</div>


<script>
	this.on('update', () => {
		this.expTable = expTable;
	});

	riot.route( _ => {
		this.update();
		return;
	} );
</script>

</compose-table-root>