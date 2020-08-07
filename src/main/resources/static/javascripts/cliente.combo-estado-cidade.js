var Brewer = Brewer || {};

Brewer.ComboEstado = (function(){
	
	function ComboEstado() {
		this.combo = $('#select-estado');
		this.emitter = $({});
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	ComboEstado.prototype.iniciar = function() {
		this.combo.on('change', onEstadoAlterado.bind(this));
	}
	
	function onEstadoAlterado() {
		this.emitter.trigger('alterado', this.combo.val());
		console.log('estado selecionado', this.combo.val());
	}
	
	return ComboEstado;
	
}());

Brewer.ComboCidade = (function() {
	
	function ComboCidade(comboEstado) {
		this.comboEstado = comboEstado;
		this.combo = $('#select-cidade');
		this.imgLoading = $('.js-img-loading');
		this.inputHiddenCidadeSelecionada = $('#inputHiddenCidadeSelecionada');
	}
	
	ComboCidade.prototype.iniciar = function() {
		reset.call(this);
		this.comboEstado.on('alterado', onEstadoAlterado.bind(this));
		var codigoEstado = this.comboEstado.combo.val();
		inicializarCidades.call(this, codigoEstado);
	}
	
	function onEstadoAlterado(evento, codigoEstado) {
		this.inputHiddenCidadeSelecionada.val('');
		inicializarCidades.call(this, codigoEstado);
	}
	
	function inicializarCidades(codigoEstado){
		if (codigoEstado !== null && codigoEstado > 0) {
			console.log('codigo do Estado no combo cidade', codigoEstado);
			var resposta = $.ajax({
				url: this.combo.data('url'),
				method: 'GET',
				contentType: 'application/json',
				data: { 'estado' : codigoEstado },
				beforeSend: iniciarRequisicao.bind(this),
				complete: finalizarRequisicao.bind(this)
			});
			resposta.done(onBuscarCidadesFinalizado.bind(this));
		} else {
			reset.call(this);
		}
	}
	
	function onBuscarCidadesFinalizado(cidades){
		var options = [];
		cidades.forEach(function(cidade){
			options.push('<option value="' + cidade.codigo + '">' + cidade.nome + '</option>');
		});
		this.combo.html(options.join(''));
		this.combo.removeAttr('disabled');
		
		var codigoCidadeSelecionada = this.inputHiddenCidadeSelecionada.val();
		if (codigoCidadeSelecionada) {
			this.combo.val(codigoCidadeSelecionada);
		}
	}
	
	function reset(){
		this.combo.html('<option value="">Selecione a cidade</option>');
		this.combo.val('');
		this.combo.attr('disabled', 'disabled');
	}
	
	function iniciarRequisicao() {
		reset.call(this);
		this.imgLoading.show();
	}
	
	function finalizarRequisicao() {
		this.imgLoading.hide();
	}
	
	return ComboCidade;
	
}());


$(function(){
	
	var comboEstado = new Brewer.ComboEstado();
	comboEstado.iniciar();
	
	var comboCidade = new Brewer.ComboCidade(comboEstado);
	comboCidade.iniciar();
	
});