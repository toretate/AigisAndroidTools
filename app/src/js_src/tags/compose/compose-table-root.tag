import './compose-table.tag'
const expTable = require('../../exp_table.json');

<compose-table-root>
	<div>
		<compose-table type="{ expTable.silver }" />
		<compose-table type="{ expTable.copper }" />
		<compose-table type="{ expTable.iron }" />
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