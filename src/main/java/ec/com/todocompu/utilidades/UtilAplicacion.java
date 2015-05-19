package ec.com.todocompu.utilidades;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

public class UtilAplicacion {

	public static void enviarVariableVista(String variable, boolean valor) {
		RequestContext.getCurrentInstance().addCallbackParam(variable, valor);
	}

	public static void presentaMensaje(Severity severity, String mensaje) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(severity, "MENSAJE DEL SISTEMA", mensaje));
	}

	public static void presentaMensaje(Severity severity, String mensaje,
			String variable, boolean valor) {
		presentaMensaje(severity, mensaje);
		enviarVariableVista(variable, valor);
	}

	public static void redireccionar(String url) {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}