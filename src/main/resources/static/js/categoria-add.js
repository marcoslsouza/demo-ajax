// Submit do formulário para o controller
$("#form-add-categoria").submit(function(evt) {
	
	// Bloquear o comportamento padrão do submit
	evt.preventDefault();
	
	// O JS cria um obj
	var cat = {};
	
	// Nesse obj eu crio o que eu quiser
	cat.titulo = $("#titulo").val();
	
	$.ajax({
		method:"POST",
		url:"/categoria/save",
		data:cat, // promo, este obj tem que ter os atributos com o mesmo nome dos atributos da classe Promocao
		beforeSend: function() {
			// Removendo as mensagens
			$("span").closest(".error-span").remove(); // Remove todas as span que possuem a classe error-span
			$("#titulo").removeClass("is-invalid");
			
			// Habilita o loading
			$("#form-add-categoria").hide(); // Esconde o formulário da página
			$("#loader-form").addClass("loader").show(); // Mostra o loading
		},
		success:function() {
			// Limpar campos do formulario
			$("#form-add-categoria").each(function() {
				this.reset();
			});
			$("#titulo").text("");
			// Fim limpar campos do formulario
			$("#alert").removeClass("alert alert-danger").addClass("alert alert-success").text("Ok! Categoria cadastrada com sucesso.");
		},
		error: function(xhr) {
			$("#alert").addClass("alert alert-danger").text("Não foi possível salvar está categoria.");
		},
		statusCode: {
			422: function(xhr) {
				var errors = $.parseJSON(xhr.responseText); // Fazer o parse do documento recebido do Java, esse documento já vem convertido em JSON pela biblioteca Jakson
				$.each(errors, function(key, val) {
					$("#"+key).addClass("is-invalid");
					$("#error-"+key).addClass("invalid-feedback").append('<span class="error-span">'+val+'</span>'); // No caso, coloca dentro da tag div um span.
				});
			}
		},
		complete: function() {
			$("#loader-form").fadeOut(800, function() { // Some o loader-form
				$("#form-add-categoria").fadeIn(250); // Exibe o form-add-promo
				$("#loader-form").removeClass("loader");
			});
		}
	});
});

