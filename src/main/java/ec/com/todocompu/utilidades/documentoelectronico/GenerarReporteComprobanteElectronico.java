package ec.com.todocompu.utilidades.documentoelectronico;

import java.io.FileInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import ec.com.todocompu.utilidades.documentoelectronico.enumerador.TipoAmbienteEnum;
import ec.com.todocompu.utilidades.documentoelectronico.enumerador.TipoEmisionEnum;
import ec.com.todocompu.utilidades.documentoelectronico.enumerador.TipoImpuestoEnum;
import ec.com.todocompu.utilidades.documentoelectronico.enumerador.TipoImpuestoIvaEnum;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.InfoTributaria;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.factura.Factura;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.factura.Factura.InfoFactura;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.notadebito.ImpuestoNotaDebito;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.notadebito.NotaDebito;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.notadebito.NotaDebito.InfoNotaDebito;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.notadecredito.NotaCredito;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.notadecredito.NotaCredito.InfoNotaCredito;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.notadecredito.TotalConImpuestos;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.rentencion.ComprobanteRetencion;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.rentencion.ComprobanteRetencion.InfoCompRetencion;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.reporte.ComprobanteRetencionReporte;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.reporte.DetallesAdicionalesReporte;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.reporte.FacturaReporte;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.reporte.NotaCreditoReporte;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.reporte.NotaDebitoReporte;
import ec.com.todocompu.utilidades.documentoelectronico.modelo.reporte.TotalComprobante;

public class GenerarReporteComprobanteElectronico {

	public static void generarReporteElectronico(String claveAcceso,
			String numeroAutorizacion, String fechaAutorizacion,
			Node contenidoComprobanteXml) {
		try {
			JasperPrint jasperPrint = null;
			String tipoComprobante = claveAcceso.substring(8, 10);
			if (tipoComprobante.compareTo("01") == 0) {
				jasperPrint = llenarYcompilarReportesElectronicoFactura(
						new FacturaReporte((Factura) unmarshal(Factura.class,
								contenidoComprobanteXml)), numeroAutorizacion,
						fechaAutorizacion);
			} else if (tipoComprobante.compareTo("04") == 0) {
				jasperPrint = llenarYcompilarReportesElectronicoNotaDebito(
						new NotaDebitoReporte((NotaDebito) unmarshal(
								NotaDebito.class, contenidoComprobanteXml)),
						numeroAutorizacion, fechaAutorizacion);
			} else if (tipoComprobante.compareTo("05") == 0) {
				jasperPrint = llenarYcompilarReportesElectronicoNotaCredito(
						new NotaCreditoReporte((NotaCredito) unmarshal(
								NotaCredito.class, contenidoComprobanteXml)),
						numeroAutorizacion, fechaAutorizacion);
			} else if (tipoComprobante.compareTo("07") == 0) {
				jasperPrint = llenarYcompilarReportesElectronicoComprobanteRetencion(
						new ComprobanteRetencionReporte(
								(ComprobanteRetencion) unmarshal(
										ComprobanteRetencion.class,
										contenidoComprobanteXml)),
						numeroAutorizacion, fechaAutorizacion);
			}

			JRPdfExporter jrPdfExporter = new JRPdfExporter();

			jrPdfExporter
					.setExporterInput(new SimpleExporterInput(jasperPrint));

			jrPdfExporter
					.setExporterOutput(new SimpleOutputStreamExporterOutput(
							System.getProperty("java.io.tmpdir") + "/"
									+ claveAcceso.trim() + ".pdf"));

			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			jrPdfExporter.setConfiguration(configuration);

			jrPdfExporter.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JasperPrint llenarYcompilarReportesElectronicoFactura(
			FacturaReporte facturaReporte, String numeroAutorizacion,
			String fechaAutorizacion) {
		try {
			String ruta = "/opt/shrimpSoft/reportes/reportesComprobantesElectronicos/reportComprobanteFacturaRide.jasper";

			FileInputStream is = null;
			JRDataSource dataSource = new JRBeanCollectionDataSource(
					facturaReporte.getDetallesAdiciones());

			is = new FileInputStream(ruta);
			JasperPrint jLlenaReporte = JasperFillManager.fillReport(
					is,
					obtenerMapaParametrosReportes(
							obtenerParametrosInfoTriobutaria(facturaReporte
									.getFactura().getInfoTributaria(),
									numeroAutorizacion, fechaAutorizacion),
							obtenerInfoFactura(facturaReporte.getFactura()
									.getInfoFactura(), facturaReporte)),
					dataSource);
			return jLlenaReporte;
		} catch (Exception e) {
			return null;
		}
	}

	public static JasperPrint llenarYcompilarReportesElectronicoNotaDebito(
			NotaDebitoReporte notaDebitoReporte, String numeroAutorizacion,
			String fechaAutorizacion) {
		try {
			String ruta = "/opt/shrimpSoft/reportes/reportesComprobantesElectronicos/reportComprobanteNotaDebitoRide.jasper";

			FileInputStream is = null;
			JRDataSource dataSource = new JRBeanCollectionDataSource(
					notaDebitoReporte.getDetallesAdiciones());

			is = new FileInputStream(ruta);
			JasperPrint jLlenaReporte = JasperFillManager.fillReport(
					is,
					obtenerMapaParametrosReportes(
							obtenerParametrosInfoTriobutaria(notaDebitoReporte
									.getNotaDebito().getInfoTributaria(),
									numeroAutorizacion, fechaAutorizacion),
							obtenerInfoND(notaDebitoReporte.getNotaDebito()
									.getInfoNotaDebito())), dataSource);

			return jLlenaReporte;
		} catch (Exception e) {
			return null;
		}
	}

	public static JasperPrint llenarYcompilarReportesElectronicoNotaCredito(
			NotaCreditoReporte notaCreditoReporte, String numeroAutorizacion,
			String fechaAutorizacion) {
		try {
			String ruta = "/opt/shrimpSoft/reportes/reportesComprobantesElectronicos/reportComprobanteNotaCreditoRide.jasper";
			FileInputStream is = null;
			net.sf.jasperreports.engine.JRDataSource dataSource = new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(
					notaCreditoReporte.getDetallesAdiciones());
			is = new FileInputStream(ruta);
			net.sf.jasperreports.engine.JasperPrint jLlenaReporte = net.sf.jasperreports.engine.JasperFillManager
					.fillReport(
							is,
							obtenerMapaParametrosReportes(
									obtenerParametrosInfoTriobutaria(
											notaCreditoReporte.getNotaCredito()
													.getInfoTributaria(),
											numeroAutorizacion,
											fechaAutorizacion),
									obtenerInfoNC(notaCreditoReporte
											.getNotaCredito()
											.getInfoNotaCredito(),
											notaCreditoReporte)), dataSource);

			return jLlenaReporte;
		} catch (Exception e) {
			return null;
		}
	}

	public static JasperPrint llenarYcompilarReportesElectronicoComprobanteRetencion(
			ComprobanteRetencionReporte comprobanteRetencionReporte,
			String numeroAutorizacion, String fechaAutorizacion) {
		try {
			String rutaReporte = "/opt/shrimpSoft/reportes/reportesComprobantesElectronicos/reportComprobanteRetencionRide.jasper";

			JRDataSource dataSource = new JRBeanCollectionDataSource(
					comprobanteRetencionReporte.getDetallesAdiciones());

			return JasperFillManager
					.fillReport(
							rutaReporte,
							obtenerMapaParametrosReportes(
									obtenerParametrosInfoTriobutaria(
											comprobanteRetencionReporte
													.getComprobanteRetencion()
													.getInfoTributaria(),
											numeroAutorizacion,
											fechaAutorizacion),
									obtenerInfoCompRetencion(comprobanteRetencionReporte
											.getComprobanteRetencion()
											.getInfoCompRetencion())),
							dataSource);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> obtenerMapaParametrosReportes(
			Map<String, Object> mapa1, Map<String, Object> mapa2) {
		mapa1.putAll(mapa2);
		return mapa1;
	}

	public static Map<String, Object> obtenerParametrosInfoTriobutaria(
			InfoTributaria infoTributaria, String numAut, String fechaAut) {
		Map<String, Object> param = new HashMap<String, Object>();
		String subReporte = "/opt/shrimpSoft/reportes/reportesComprobantesElectronicos/";
		param.put("dirPhoto", "/opt/shrimpSoft/imagenes/CorpAcua.jpg");
		param.put("RUC", infoTributaria.getRuc());
		param.put("CLAVE_ACC", infoTributaria.getClaveAcceso());
		param.put("RAZON_SOCIAL", infoTributaria.getRazonSocial());
		param.put("DIR_MATRIZ", infoTributaria.getDirMatriz());
		param.put("SUBREPORT_DIR", subReporte);
		param.put("TIPO_EMISION", obtenerTipoEmision(infoTributaria));
		param.put("NUM_AUT", numAut);
		param.put("FECHA_AUT", fechaAut);
		param.put("NUM_FACT",
				infoTributaria.getEstab() + "-" + infoTributaria.getPtoEmi()
						+ "-" + infoTributaria.getSecuencial());
		param.put("AMBIENTE", obtenerAmbiente(infoTributaria));
		param.put("NOM_COMERCIAL", infoTributaria.getNombreComercial());
		return param;
	}

	public static Map<String, Object> obtenerInfoCompRetencion(
			InfoCompRetencion infoComp) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("DIR_SUCURSAL", infoComp.getDirEstablecimiento());
		param.put("RS_COMPRADOR", infoComp.getRazonSocialSujetoRetenido());
		param.put("RUC_COMPRADOR", infoComp.getIdentificacionSujetoRetenido());
		param.put("FECHA_EMISION", infoComp.getFechaEmision());
		param.put("CONT_ESPECIAL", infoComp.getContribuyenteEspecial());
		param.put("LLEVA_CONTABILIDAD", infoComp.getObligadoContabilidad());
		param.put("EJERCICIO_FISCAL", infoComp.getPeriodoFiscal());
		return param;
	}

	public static java.util.Map<String, Object> obtenerInfoFactura(
			InfoFactura infoFactura, FacturaReporte fact) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("DIR_SUCURSAL", infoFactura.getDirEstablecimiento());
		param.put("CONT_ESPECIAL", infoFactura.getContribuyenteEspecial());
		param.put("LLEVA_CONTABILIDAD", infoFactura.getObligadoContabilidad());
		param.put("RS_COMPRADOR", infoFactura.getRazonSocialComprador());
		param.put("RUC_COMPRADOR", infoFactura.getIdentificacionComprador());
		param.put("FECHA_EMISION", infoFactura.getFechaEmision());
		param.put("GUIA", infoFactura.getGuiaRemision());
		TotalComprobante tc = getTotales(infoFactura);
		param.put("VALOR_TOTAL", infoFactura.getImporteTotal());
		param.put("DESCUENTO", infoFactura.getTotalDescuento());
		param.put("IVA", tc.getIva12());
		param.put("IVA_0", tc.getSubtotal0());
		param.put("IVA_12", tc.getSubtotal12());
		param.put("ICE", tc.getTotalIce());
		param.put("NO_OBJETO_IVA", tc.getSubtotalNoSujetoIva());
		param.put("SUBTOTAL", infoFactura.getTotalSinImpuestos());
		if (infoFactura.getPropina() != null) {
			param.put("PROPINA", infoFactura.getPropina().toString());
		}
		param.put("TOTAL_DESCUENTO", calcularDescuento(fact));
		return param;
	}

	public static Map<String, Object> obtenerInfoND(InfoNotaDebito notaDebito) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("DIR_SUCURSAL", notaDebito.getDirEstablecimiento());
		param.put("CONT_ESPECIAL", notaDebito.getContribuyenteEspecial());
		param.put("LLEVA_CONTABILIDAD", notaDebito.getObligadoContabilidad());
		param.put("RS_COMPRADOR", notaDebito.getRazonSocialComprador());
		param.put("RUC_COMPRADOR", notaDebito.getIdentificacionComprador());
		param.put("FECHA_EMISION", notaDebito.getFechaEmision());
		TotalComprobante tc = getTotalesND(notaDebito);
		param.put("IVA_0", tc.getSubtotal0());
		param.put("IVA_12", tc.getSubtotal12());
		param.put("ICE", tc.getTotalIce());
		param.put("TOTAL", notaDebito.getValorTotal());
		param.put("IVA", tc.getIva12());
		param.put("NO_OBJETO_IVA", tc.getSubtotalNoSujetoIva());
		param.put("NUM_DOC_MODIFICADO", notaDebito.getNumDocModificado());
		param.put("FECHA_EMISION_DOC_SUSTENTO",
				notaDebito.getFechaEmisionDocSustento());
		param.put("DOC_MODIFICADO",
				obtenerDocumentoModificado(notaDebito.getCodDocModificado()));
		param.put("TOTAL_SIN_IMP", notaDebito.getTotalSinImpuestos());
		return param;
	}

	public static Map<String, Object> obtenerInfoNC(InfoNotaCredito infoNC,
			NotaCreditoReporte nc) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("DIR_SUCURSAL", infoNC.getDirEstablecimiento());
		param.put("CONT_ESPECIAL", infoNC.getContribuyenteEspecial());
		param.put("LLEVA_CONTABILIDAD", infoNC.getObligadoContabilidad());
		param.put("RS_COMPRADOR", infoNC.getRazonSocialComprador());
		param.put("RUC_COMPRADOR", infoNC.getIdentificacionComprador());
		param.put("FECHA_EMISION", infoNC.getFechaEmision());
		TotalComprobante tc = getTotalesNC(infoNC);
		param.put("IVA_0", tc.getSubtotal0());
		param.put("IVA_12", tc.getSubtotal12());
		param.put("ICE", tc.getTotalIce());
		param.put("VALOR_TOTAL", infoNC.getValorModificacion());
		param.put("IVA", tc.getIva12());
		param.put("SUBTOTAL", infoNC.getTotalSinImpuestos());
		param.put("NO_OBJETO_IVA", tc.getSubtotalNoSujetoIva());
		param.put("NUM_DOC_MODIFICADO", infoNC.getNumDocModificado());
		param.put("FECHA_EMISION_DOC_SUSTENTO",
				infoNC.getFechaEmisionDocSustento());
		param.put("DOC_MODIFICADO",
				obtenerDocumentoModificado(infoNC.getCodDocModificado()));
		param.put("TOTAL_DESCUENTO", obtenerTotalDescuento(nc));
		param.put("RAZON_MODIF", infoNC.getMotivo());
		return param;
	}

	public static String obtenerTotalDescuento(NotaCreditoReporte nc) {
		BigDecimal descuento = new BigDecimal(0);
		for (DetallesAdicionalesReporte detalle : nc.getDetallesAdiciones()) {
			descuento = descuento.add(new BigDecimal(detalle.getDescuento()));
		}
		return descuento.toString();
	}

	public static String calcularDescuento(FacturaReporte fact) {
		BigDecimal descuento = new BigDecimal(0);
		for (DetallesAdicionalesReporte detalle : fact.getDetallesAdiciones()) {
			descuento = descuento.add(new BigDecimal(detalle.getDescuento()));
		}
		return descuento.toString();
	}

	public static String obtenerTipoEmision(InfoTributaria infoTributaria) {
		if (infoTributaria.getTipoEmision().equals("2")) {
			return TipoEmisionEnum.CONTINGENCIA.getCode();
		}
		if (infoTributaria.getTipoEmision().equals("1")) {
			return TipoEmisionEnum.NORMAL.getCode();
		}
		return null;
	}

	public static String obtenerAmbiente(InfoTributaria infoTributaria) {
		if (infoTributaria.getAmbiente().equals("2")) {
			return TipoAmbienteEnum.PRODUCCION.toString();
		}
		return TipoAmbienteEnum.PRUEBAS.toString();
	}

	public static TotalComprobante getTotalesNC(InfoNotaCredito infoNc) {
		BigDecimal totalIva12 = new BigDecimal(0.0D);
		BigDecimal totalIva0 = new BigDecimal(0.0D);
		BigDecimal iva12 = new BigDecimal(0.0D);
		BigDecimal totalICE = new BigDecimal(0.0D);
		BigDecimal totalSinImpuesto = new BigDecimal(0.0D);
		TotalComprobante tc = new TotalComprobante();
		for (TotalConImpuestos.TotalImpuesto ti : infoNc.getTotalConImpuestos()
				.getTotalImpuesto()) {
			Integer cod = new Integer(ti.getCodigo());
			if ((TipoImpuestoEnum.IVA.getCode() == cod.intValue())
					&& (TipoImpuestoIvaEnum.IVA_VENTA_12.getCode().equals(ti
							.getCodigoPorcentaje()))) {
				totalIva12 = totalIva12.add(ti.getBaseImponible());
				iva12 = iva12.add(ti.getValor());
			}
			if ((TipoImpuestoEnum.IVA.getCode() == cod.intValue())
					&& (TipoImpuestoIvaEnum.IVA_VENTA_0.getCode().equals(ti
							.getCodigoPorcentaje()))) {
				totalIva0 = totalIva0.add(ti.getBaseImponible());
			}
			if ((TipoImpuestoEnum.IVA.getCode() == cod.intValue())
					&& (TipoImpuestoIvaEnum.IVA_VENTA_EXCENTO.getCode()
							.equals(ti.getCodigoPorcentaje()))) {
				totalSinImpuesto = totalSinImpuesto.add(ti.getBaseImponible());
			}
			if (TipoImpuestoEnum.ICE.getCode() == cod.intValue()) {
				totalICE = totalICE.add(ti.getValor());
			}
		}
		tc.setIva12(iva12.toString());
		tc.setSubtotal0(totalIva0.toString());
		tc.setSubtotal12(totalIva12.toString());
		tc.setTotalIce(totalICE.toString());
		tc.setSubtotal(totalIva0.add(totalIva12));
		tc.setSubtotalNoSujetoIva(totalSinImpuesto.toString());
		return tc;
	}

	public static TotalComprobante getTotalesND(InfoNotaDebito infoNotaDebito) {
		BigDecimal totalIva12 = new BigDecimal(0.0D);
		BigDecimal totalIva0 = new BigDecimal(0.0D);
		BigDecimal totalICE = new BigDecimal(0.0D);
		BigDecimal iva12 = new BigDecimal(0.0D);
		BigDecimal totalSinImpuesto = new BigDecimal(0.0D);
		TotalComprobante tc = new TotalComprobante();
		for (ImpuestoNotaDebito ti : infoNotaDebito.getImpuestos()
				.getImpuesto()) {
			Integer cod = new Integer(ti.getCodigo());
			if ((TipoImpuestoEnum.IVA.getCode() == cod.intValue())
					&& (TipoImpuestoIvaEnum.IVA_VENTA_12.getCode().equals(ti
							.getCodigoPorcentaje()))) {
				totalIva12 = totalIva12.add(ti.getBaseImponible());
				iva12 = iva12.add(ti.getValor());
			}
			if ((TipoImpuestoEnum.IVA.getCode() == cod.intValue())
					&& (TipoImpuestoIvaEnum.IVA_VENTA_0.getCode().equals(ti
							.getCodigoPorcentaje()))) {
				totalIva0 = totalIva0.add(ti.getBaseImponible());
			}
			if ((TipoImpuestoEnum.IVA.getCode() == cod.intValue())
					&& (TipoImpuestoIvaEnum.IVA_VENTA_EXCENTO.getCode()
							.equals(ti.getCodigoPorcentaje()))) {
				totalSinImpuesto = totalSinImpuesto.add(ti.getBaseImponible());
			}
			if (TipoImpuestoEnum.ICE.getCode() == cod.intValue()) {
				totalICE = totalICE.add(ti.getValor());
			}
		}
		tc.setSubtotal0(totalIva0.toString());
		tc.setSubtotal12(totalIva12.toString());
		tc.setTotalIce(totalICE.toString());
		tc.setIva12(iva12.toString());
		tc.setSubtotalNoSujetoIva(totalSinImpuesto.toPlainString());
		return tc;
	}

	public static TotalComprobante getTotales(InfoFactura infoFactura) {
		BigDecimal totalIva12 = new BigDecimal(0.0D);
		BigDecimal totalIva0 = new BigDecimal(0.0D);
		BigDecimal iva12 = new BigDecimal(0.0D);
		BigDecimal totalICE = new BigDecimal(0.0D);
		BigDecimal totalSinImpuesto = new BigDecimal(0.0D);
		TotalComprobante tc = new TotalComprobante();
		for (Factura.InfoFactura.TotalConImpuestos.TotalImpuesto ti : infoFactura
				.getTotalConImpuestos().getTotalImpuesto()) {
			Integer cod = new Integer(ti.getCodigo());
			if ((TipoImpuestoEnum.IVA.getCode() == cod.intValue())
					&& (TipoImpuestoIvaEnum.IVA_VENTA_12.getCode().equals(ti
							.getCodigoPorcentaje()))) {
				totalIva12 = totalIva12.add(ti.getBaseImponible());
				iva12 = iva12.add(ti.getValor());
			}
			if ((TipoImpuestoEnum.IVA.getCode() == cod.intValue())
					&& (TipoImpuestoIvaEnum.IVA_VENTA_0.getCode().equals(ti
							.getCodigoPorcentaje()))) {
				totalIva0 = totalIva0.add(ti.getBaseImponible());
			}
			if ((TipoImpuestoEnum.IVA.getCode() == cod.intValue())
					&& (TipoImpuestoIvaEnum.IVA_VENTA_EXCENTO.getCode()
							.equals(ti.getCodigoPorcentaje()))) {
				totalSinImpuesto = totalSinImpuesto.add(ti.getBaseImponible());
			}
			if (TipoImpuestoEnum.ICE.getCode() == cod.intValue()) {
				totalICE = totalICE.add(ti.getValor());
			}
		}
		tc.setIva12(iva12.toString());
		tc.setSubtotal0(totalIva0.toString());
		tc.setSubtotal12(totalIva12.toString());
		tc.setTotalIce(totalICE.toString());
		tc.setSubtotal(totalIva0.add(totalIva12));
		tc.setSubtotalNoSujetoIva(totalSinImpuesto.toString());
		return tc;
	}

	public static Object unmarshal(Class<?> clase, Node contenidoXml)
			throws Exception {
		return JAXBContext
				.newInstance(clase)
				.createUnmarshaller()
				.unmarshal(
						new InputSource(new StringReader(contenidoXml
								.getTextContent())));
	}

	public static String obtenerDocumentoModificado(String codDoc) {
		if ("01".equals(codDoc)) {
			return "FACTURA";
		}
		if ("04".equals(codDoc)) {
			return "NOTA DE CRÉDITO";
		}
		if ("05".equals(codDoc)) {
			return "NOTA DE DÉBITO";
		}
		if ("06".equals(codDoc)) {
			return "GUÍA REMISIÓN";
		}
		if ("07".equals(codDoc)) {
			return "COMPROBANTE DE RETENCIÓN";
		}
		return null;
	}
}
