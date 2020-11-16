// Submit do formulário para o controller
$("#form-add-promo").submit(function(evt) {
	
	// Bloquear o comportamento padrão do submit
	evt.preventDefault();
	
	// O JS cria um obj
	var promo = {};
	
	// Nesse obj eu crio o que eu quiser
	promo.linkPromocao = $("#linkPromocao").val();
	promo.descricao = $("#descricao").val();
	promo.preco = $("#preco").val();
	promo.titulo = $("#titulo").val();
	promo.categoria = $("#categoria").val();
	promo.linkImagem = $("#linkImagem").attr("src");
	promo.site = $("#site").text();
	
	console.log("promo > ", promo);
	
	$.ajax({
		method:"POST",
		url:"/promocao/save",
		data:promo, // promo, este obj tem que ter os atributos com o mesmo nome dos atributos da classe Promocao
		beforeSend: function() {
			// Removendo as mensagens
			$("span").closest(".error-span").remove(); // Remove todas as span que possuem a classe error-span
			$("#categoria").removeClass("is-invalid");
			$("#preco").removeClass("is-invalid");
			$("#linkPromocao").removeClass("is-invalid");
			$("#titulo").removeClass("is-invalid");
			
			// Habilita o loading
			$("#form-add-promo").hide(); // Esconde o formulário da página
			$("#loader-form").addClass("loader").show(); // Mostra o loading
		},
		success:function() {
			// Limpar campos do formulario
			$("#form-add-promo").each(function() {
				this.reset();
			});
			$("#linkImagem").attr("src", "/images/promo-dark.png");
			$("#site").text("");
			// Fim limpar campos do formulario
			$("#alert").removeClass("alert alert-danger").addClass("alert alert-success").text("Ok! Promoção cadastrada com sucesso.");
		},
		error: function(xhr) {
			console.log("> error: ", xhr.responseText);
			$("#alert").addClass("alert alert-danger").text("Não foi possível salvar está promoção.");
		},
		statusCode: {
			422: function(xhr) {
				console.log("status error:", xhr.status);
				var errors = $.parseJSON(xhr.responseText); // Fazer o parse do documento recebido do Java, esse documento já vem convertido em JSON pela biblioteca Jakson
				$.each(errors, function(key, val) {
					$("#"+key).addClass("is-invalid");
					$("#error-"+key).addClass("invalid-feedback").append('<span class="error-span">'+val+'</span>'); // No caso, coloca dentro da tag div um span.
				});
			}
		},
		complete: function() {
			$("#loader-form").fadeOut(800, function() { // Some o loader-form
				$("#form-add-promo").fadeIn(250); // Exibe o form-add-promo
				$("#loader-form").removeClass("loader");
			});
		}
	});
});

// Função para capturar as metatags
$("#linkPromocao").on("change", function() {
	var url = $(this).val();
	if(url.length > 7) {
		$.ajax({
			method:"POST",
			url:"/meta/info?url="+url, // Acesso ao controller
			cache:false, // Não fazer uso do cache
			beforeSend:function() { // Limpar os componentes antes de qualquer requisição
				$("#alert").removeClass("alert alert-danger alert-success").text("");
				$("#titulo").val("");
				$("#site").html("");
				$("#linkImagem").attr("src", "");
				$("#loader-img").addClass("loader");
			},
			success:function(data) {
				console.log(data);
				$("#titulo").val(data["title"]);
				$("#site").html(data["site"].replace("@", ""));
				$("#linkImagem").attr("src", data["image"]);
			},
			statusCode:{
				404: function() {
					// Acessando as classes do bootstrap 4 do componente
					$("#alert").addClass("alert alert-danger").text("Nunhuma informação pode ser recuperada dessa URL");
					$("#linkImagem").attr("src", "/images/promo-dark.png");
				},
				500: function() {
					// Acessando as classes do bootstrap 4 do componente
					$("#alert").addClass("alert alert-danger").text("URL não existe");
					$("#linkImagem").attr("src", "/images/promo-dark.png");
				},
				error: function() {
					// Acessando as classes do bootstrap 4 do componente
					$("#alert").addClass("alert alert-danger").text("Algo deu errado tente mais tarde");
					$("#linkImagem").attr("src", "/images/promo-dark.png");
				}
			},
			complete: function() {
				$("#loader-img").removeClass("loader");
			}
		});
	}
});