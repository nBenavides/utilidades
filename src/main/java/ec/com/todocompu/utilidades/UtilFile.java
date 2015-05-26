package ec.com.todocompu.utilidades;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import ec.com.todocompu.utilidades.documentoelectronico.GenerarReporteComprobanteElectronico;

public class UtilFile {

	private static FileInputStream in;

	public static void crearXmlFromByte(String nombreArchivo,
			byte[] contenidoByte) {
		String ruta = System.getProperty("java.io.tmpdir") + "/"
				+ nombreArchivo.trim() + ".xml";
		File xmlFile = new File(ruta);
		BufferedWriter contenidoString;
		try {
			contenidoString = new BufferedWriter(new FileWriter(xmlFile));
			contenidoString.write(new String(contenidoByte));
			contenidoString.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void crearZip(String nombreZip, List<String> archivos) {

		try {
			FileOutputStream out = new FileOutputStream(
					System.getProperty("java.io.tmpdir") + "/"
							+ nombreZip.trim() + ".zip");
			ZipOutputStream zipOut = new ZipOutputStream(out);

			for (String archivo : archivos) {
				// AGREGAR EL XML
				byte b[] = new byte[2048];
				String inputFile = System.getProperty("java.io.tmpdir") + "/"
						+ archivo.trim() + ".xml";
				in = new FileInputStream(inputFile);
				ZipEntry entry = new ZipEntry(inputFile);
				zipOut.putNextEntry(entry);
				int len = 0;
				while ((len = in.read(b)) != -1) {
					zipOut.write(b, 0, len);
				}

				// AGREGAR EL PDF
				b = new byte[2048];
				inputFile = System.getProperty("java.io.tmpdir") + "/"
						+ archivo.trim() + ".pdf";
				in = new FileInputStream(inputFile);
				entry = new ZipEntry(inputFile);
				zipOut.putNextEntry(entry);
				len = 0;
				while ((len = in.read(b)) != -1) {
					zipOut.write(b, 0, len);
				}
			}
			zipOut.closeEntry();
			zipOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void generarComprobantePdfFromXml(String claveAcceso,
			String numeroAutorizacion, String fechaAutorizacion) {

		Node contenidoComprobanteXml;
		try {
			contenidoComprobanteXml = DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(new File(System.getProperty("java.io.tmpdir") + "/"
							+ claveAcceso.trim() + ".xml"))
					.getElementsByTagName("comprobante").item(0);

			GenerarReporteComprobanteElectronico.generarReporteElectronico(
					claveAcceso, numeroAutorizacion, fechaAutorizacion,
					contenidoComprobanteXml);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}
}