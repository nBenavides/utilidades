package ec.com.todocompu.utilidades;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;

public class UtilMail {

	public static void envioCorreo(String destinatarios, String asunto,
			String detalle, List<File> listAdjunto, String emailEmisor,
			String passEmail) throws Exception {

		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", true);

		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		properties.put("mail.smtp.starttls.enable", true);

		Session session = Session.getInstance(properties, null);

		Message mensaje = new MimeMessage(session);
		mensaje.setFrom(new InternetAddress(emailEmisor));

		try {
			mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(
					destinatarios));
			// Message.RecipientType.CC; con copia
			// Message.RecipientType.BCC; con copia oculta
		} catch (Exception e) {
			e.printStackTrace();
		}

		mensaje.setSubject(asunto);
		mensaje.setSentDate(new Date());
		Multipart multipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(detalle);
		multipart.addBodyPart(messageBodyPart);
		for (File adjunto : listAdjunto) {
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(new FileDataSource(
					adjunto)));
			messageBodyPart.setFileName(adjunto.getName());
			multipart.addBodyPart(messageBodyPart);
		}

		mensaje.setContent(multipart);
		System.clearProperty("javax.net.ssl.keyStore");
		System.clearProperty("javax.net.ssl.keyStorePassword");
		System.clearProperty("javax.net.ssl.trustStore");
		System.clearProperty("javax.net.ssl.trustStorePassword");

		SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
		try {
			transport.connect(emailEmisor, passEmail);
			transport.sendMessage(mensaje, mensaje.getAllRecipients());
		} finally {
			transport.close();
		}
	}
}
