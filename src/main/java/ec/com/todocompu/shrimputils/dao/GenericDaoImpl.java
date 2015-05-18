package ec.com.todocompu.shrimputils.dao;

import java.io.Serializable;
import java.util.List;

import javax.swing.JOptionPane;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;



public class GenericDaoImpl<T, K extends Serializable> implements
		GenericDao<T, K> {

	@Autowired
	public SessionFactory sessionFactory;

	public Session session() {
		return sessionFactory.getCurrentSession();
	}

	public void insertar(T t) {
		session().save(t);
	}

	public void actualizar(T t) {
		session().update(t);
	}

	@SuppressWarnings("unchecked")
	public T obtenerObjeto(String consulta, Object[] valoresConsulta,
			Boolean presentarMensaje) {
		boolean validacion = false;
		List<T> list = null;
		Query query = (Query) session().createQuery(consulta);

		if (valoresConsulta != null) {
			for (int i = 0; i < valoresConsulta.length; i++) {
				if (valoresConsulta[i] != null) {
					if (valoresConsulta[i] instanceof String) {
						if ((String.valueOf(valoresConsulta[i]).startsWith("%")
								&& String.valueOf(valoresConsulta[i]).endsWith(
										"%") && String.valueOf(
								valoresConsulta[i]).length() >= 5)
								|| (!String.valueOf(valoresConsulta[i])
										.startsWith("%")
										&& !String.valueOf(valoresConsulta[i])
												.endsWith("%") && String
										.valueOf(valoresConsulta[i]).length() >= 3)) {
							// valoresConsulta[i] = String.valueOf(
							// valoresConsulta[i]).toUpperCase();
						} else {
							validacion = true;
							break;
						}
					}
					query.setParameter(String.valueOf(i + 1),
							valoresConsulta[i]);
				}
			}
		}
		if (validacion) {
			JOptionPane
					.showMessageDialog(null,
							"PARA CUALQUIER BÚSQUEDA DEBE INGRESAR MAS DE 2 CARACTERES");
		} else {
			list = query.list();
			if (list.isEmpty() && presentarMensaje) {
				JOptionPane.showMessageDialog(null,
						"NO SE ENCONTRO NINGUNA COINCIDENCIA");
			} else if (!list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}
}
