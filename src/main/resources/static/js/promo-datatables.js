$(document).ready(function() {
	// Formatação de datas
	// https://momentjs.com/
	// https://cdnjs.com/ (cdn javascript)
	moment.locale('pt-br');
	
	let table = $("#table-server").DataTable({
		processing:true,
		serverSide: true,
		responsive:true,
		lengthMenu:[10, 15, 20, 25],
		ajax: {
			url: "/promocao/datatables/server",
			data: "data"
		},
		columns: [
			{data: 'id', visible: false},
			{data: 'titulo'},
			{data: 'site'},
			{data: 'linkPromocao'},
			{data: 'descricao'},
			{data: 'linkImagem'},
			{data: 'preco', render: $.fn.dataTable.render.number('.', ',', 2, 'R$')},
			{data: 'likes'},
			{data: 'dtCadastro', render:
				function(dtCadastro) {
					return moment(dtCadastro).format('L');
				}
			},
			{data: 'categoria.titulo'}
		],
		dom: 'Bfrtip',
		buttons: [{
			text: 'Editar',
			attr: {
				id: 'btn-editar',
				type: 'button'
			},
			enabled: false
		},
		{
			text: 'Excluir',
			attr: {
				id: 'btn-excluir',
				type: 'button'
			},
			enabled: false
		}
		]
	});
	
	// marcar/desmarcar botoes ao clicar na ordenação
	$('#table-server thead').on('click', 'tr', function() {
		table.buttons().disable();
	});
	
	$('#table-server tbody').on('click', 'tr', function() {
		if($(this).hasClass('selected')) {
			$(this).removeClass('selected');
			table.buttons().disable();
		} else {
			$('tr.selected').removeClass('selected'); // Procura por uma linha que tenha a classe selected, e ao encontrar será removida a classe selected.
			$(this).addClass('selected');
			table.buttons().enable();
		}
	});
	
	$('#btn-editar').on('click', function() {
		if(isSelectedRow()) {
			$('#modal-form').modal('show');
			//let id = getPromoId();
			//alert('click no botão editar ' + id);
		}
	});
	
	$('#btn-excluir').on('click', function() {
		if(isSelectedRow()) {
			$('#modal-delete').modal('show');
			//alert('click no botão excluir');
		}
	});
	
	function getPromoId() {
		return table.row(table.$('tr.selected')).data().id;
	}
	
	function isSelectedRow() {
		let trow = table.row(table.$('tr.selected'));
		return trow.data() !== undefined;
	}
});



