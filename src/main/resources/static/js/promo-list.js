// Efeito infinite scroll (Conforme vamos descendo o scroll da página, o javascript vai fazendo requisições para novos registros, ou seja, uma páginação com scroll)
var pageNumber = 0;

$(document).ready(function() {
	$("#loader-img").hide(); // Esconde o loading, assim que a página é aberta
	$("#fim-btn").hide(); // Esconde o botão fim de página, assim que a página é aberta
});

$(window).scroll(function() { // Identifica quando estamos acionando o scroll da página ou do mouse
	var scrollTop = $(this).scrollTop(); // Altura em que esta a barra de rolagem do navegador
	var conteudo = $(document).height() - $(window).height(); // "$(document).height()" altura do conteudo da página // "$(window).height()" altura da tela
	//console.log("scrollTop: ", scrollTop, " | ", "conteudo", conteudo);
	if(scrollTop >= conteudo) {
		pageNumber++;
		setTimeout(function() { // setTimeout funciona como um deley
			loadByScrollBar(pageNumber);
		}, 200); // 200 milessegundos
	}
});

function loadByScrollBar(pageNumber) {
	var site = $("#autocomplete-input").val(); // Recupera o nome do site no input
	$.ajax({
		method:"GET",
		url:"/promocao/list/ajax",
		data:{pageNumber, site},
		beforeSend: function() {
			$("#loader-img").show(); // Exibe o loading
		},
		success: function(response) {
			console.log("response >", response.length);
			if(response.length > 150) { // Caracteres > 150
				$(".row").fadeIn(250, function() {
					$(this).append(response);
				});
			} else {// Caracteres == 0
				$("#fim-btn").show();
				$("#loader-img").removeClass("loader");
			}
		},
		error: function(xhr) {
			alert("Ops, ocorreu um erro: " + xhr.status + " - " + xhr.statusText);
		},
		complete: function() {
			$("#loader-img").hide(); // Esconde o loading
		}
	});
}

// Adicionar likes. Obs: Eventos delegados, quando fazemos uma nova páginação, os novos componentes não fazem parte do DOM anterior.
$(document).on("click", "button[id*='likes-btn-']", function() { // Evento delegado
	var id = $(this).attr("id").split("-")[2]; // Temos 3 posições "likes-btn-' + promo.id" 0 - 1 - 2
	console.log("id:", id);
	
	$.ajax({
		method:"POST",
		url:"/promocao/like/"+id,
		success: function(response) {
			$("#likes-count-"+id).text(response);
		},
		error: function(xhr) {
			alert("Ops, ocorreu um erro: " + xhr.status + ", " + xhr.statusText);
		}
	});
});

// Autocomplete
$("#autocomplete-input").autocomplete({
	source: function(request, response) { // Envia o valor do input / Recebe o valor do backend
		$.ajax({
			method:"GET",
			url:"/promocao/site",
			data: {
				termo: request.term // Recebe o valor do input
			},
			success: function(result) {
				response(result); // Atribui o valor recebido ao response
			}
		});
	}
});

// Pesquisar pelo termo
$("#autocomplete-submit").on("click", function() {
	var site = $("#autocomplete-input").val(); // Recupera o nome do site no input
	$.ajax({
		method:"GET",
		url:"/promocao/site/list",
		data:{
			site: site
		},
		beforeSend: function() {
			pageNumber = 0; // Porque iremos iniciar uma nova paginação para a consulta
			$("#fim-btn").hide(); // Ocultar o botão topo
			$(".row").fadeOut(400, function() {
				$(this).empty();
			}); // Ocultar os cards atuais
		},
		success: function(response) {
			$(".row").fadeIn(250, function() {
				$(this).append(response);
			}); // Mostar os novos cards
		},
		error: function(xhr) {
			alert("Ops, algo deu errado: "+xhr.status+", "+xhr.statusText);
		}
	});
});
