package br.com.digitalzyon.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import br.com.digitalzyon.dto.PeriodoRelatorio;
import br.com.digitalzyon.service.exception.ValidacaoException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioService {
	
	@Autowired
	private DataSource dataSource;
	
	public byte[] gerarRelatorioVendasEmitidas(PeriodoRelatorio periodoRelatorio) throws SQLException, JRException {
		
		this.validarDatas(periodoRelatorio);
		
		Date dataInicio = Date.from(LocalDateTime.of(periodoRelatorio.getDataInicio(), LocalTime.of(0, 0, 0))
				.atZone(ZoneId.systemDefault()).toInstant());
		Date dataFim = Date.from(LocalDateTime.of(periodoRelatorio.getDataFim(), LocalTime.of(23, 59, 59))
				.atZone(ZoneId.systemDefault()).toInstant());
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("format", "pdf");
		parametros.put("data_inicio", dataInicio);
		parametros.put("data_fim", dataFim);
		
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/relatorio_vendas_emitidas.jasper");
		
		Connection connection = this.dataSource.getConnection();
		
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, connection);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			connection.close();
		}
	}

	private void validarDatas(PeriodoRelatorio periodoRelatorio) throws ValidacaoException {
		if (periodoRelatorio == null) {
			throw new ValidacaoException("Informe o período!");
		} else {
			if (periodoRelatorio.getDataInicio() == null) {
				throw new ValidacaoException("Informe a data inicial!");
			}
			if (periodoRelatorio.getDataFim() == null) {
				throw new ValidacaoException("Informe a data final!");
			}
			if (periodoRelatorio.getDataInicio().isAfter(periodoRelatorio.getDataFim())) {
				throw new ValidacaoException("A data inicial não pode ser superior a data final!");
			}
		}
	}

}
