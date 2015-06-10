package ec.com.todocompu.utilidades.service;

import java.util.List;
import java.util.Map;

public interface ReporteService {

	public <T> void generarPdf(String nombreReporte,
			Map<String, Object> parametros, List<T> datos);

}
