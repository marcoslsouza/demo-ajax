$(document).ready(function() {
	// Formatação de datas
	// https://momentjs.com/
	// https://cdnjs.com/ (cdn javascript)
	moment.locale('pt-br');
	
	$("#table-server").DataTable({
		processing:true,
		serverSide: true,
		responsive:true,
		lengthMenu:[10, 15, 20, 25],
		ajax: {
			url: "/promocao/datatables/server",
			data: "data"
		},
		columns: [
			{data: 'id'},
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
		]
	});
});