package ec.com.todocompu.utilidades.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import org.springframework.stereotype.Service;

import ec.com.todocompu.utilidades.Conexion;

@Service
public class ReporteServiceImpl implements ReporteService {

	public <T> void generarPdf(String nombreReporte,
			Map<String, Object> parametros, List<T> datos) {
		try {
			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.addHeader("Content-disposition", "attachment; filename="
					+ nombreReporte + ".pdf");

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					"/opt/development/reportes/" + nombreReporte + ".jasper",
					parametros, Conexion.getConnection());

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
					response.getOutputStream()));
			exporter.setConfiguration(new SimplePdfExporterConfiguration());
			exporter.exportReport();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
