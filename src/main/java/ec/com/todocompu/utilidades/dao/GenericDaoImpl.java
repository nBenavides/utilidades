package ec.com.todocompu.utilidades.dao;

import static ec.com.todocompu.utilidades.UtilAplicacion.presentaMensaje;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericDaoImpl<T, K extends Serializable> implements
		GenericDao<T, K> {

	@Autowired
	public SessionFactory sessionFactory;

	public boolean actualizar(T t) {
		try {
			Field[] field = t.getClass().getDeclaredFields();
			for (int i = 0; i < field.length; i++) {
				if (field[i].getType() == new String().getClass()
						&& field[i].getName().compareTo("password") != 0) {
					field[i].setAccessible(true);
					String obj = String.valueOf(field[i].get(t));
					if (obj.isEmpty())
						obj = null;
					else
						obj = obj.toUpperCase();
					field[i].set(t, obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session().update(t);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"ACTUALIZÓ EL REGISTRO CON ÉXITO", "cerrar", true);
		return true;
	}

	public Boolean comprobarIndiceUnico(T t, String atributo, String valor) {
		valor = valor.toUpperCase();
		T objetoBuscado = obtenerPorAtributo(t.getClass().getName(), atributo,
				valor);
		if (objetoBuscado != null) {
			try {
				String idBuscado = String.valueOf(objetoBuscado.getClass()
						.getMethod("getId").invoke(objetoBuscado));
				String idModificado = String.valueOf(t.getClass()
						.getMethod("getId").invoke(t));

				if (idBuscado.compareTo(idModificado) == 0) {
					session().evict(objetoBuscado);
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public Object contar(Class<T> type) {
		return session().createCriteria(type.getName())
				.setProjection(Projections.count("id")).uniqueResult();
	}

	// public Object contar(String consulta, Object[] valores) {
	// Query query = (Query) session().createQuery(consulta);
	// if (valores != null) {
	// for (Integer i = 0; i < valores.length; i++) {
	// Integer iparameter = i + 1;
	// query.setParameter(iparameter.toString(), valores[i]);
	// }
	// }
	// return query.uniqueResult();
	// }

	// public void evict(T t) {
	// session().evict(t);
	// }

	public void insertar(T t) {
		try {
			Field[] field = t.getClass().getDeclaredFields();
			for (int i = 0; i < field.length; i++) {
				if (field[i].getType() == new String().getClass()
						&& field[i].getName().compareTo("password") != 0) {
					field[i].setAccessible(true);
					String obj = String.valueOf(field[i].get(t));
					if (obj.isEmpty())
						obj = null;
					else
						obj = obj.toUpperCase();
					field[i].set(t, obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session().save(t);
		presentaMensaje(FacesMessage.SEVERITY_INFO,
				"INSERTÓ EL REGISTRO CON ÉXITO", "cerrar", true);
	}

	@SuppressWarnings("unchecked")
	public T obtenerPorAtributo(String entidad, String Atributo,
			String valorAtributo) {
		return (T) session().createCriteria(entidad)
				.add(Restrictions.eq(Atributo, valorAtributo)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> obtenerLista(String consulta, Object[] valoresConsulta,
			boolean mensaje, Object[] valoresInicializar) {
		boolean validacion = false;
		List<T> list = null;
		Query query = (Query) session().createQuery(consulta);
		if (valoresConsulta != null)
			for (int i = 0; i < valoresConsulta.length; i++)
				if (valoresConsulta[i] != null) {
					// if (valoresConsulta[i] instanceof String) {
					// if ((String.valueOf(valoresConsulta[i]).startsWith("%")
					// && String.valueOf(valoresConsulta[i]).endsWith(
					// "%") && String.valueOf(
					// valoresConsulta[i]).length() >= 5)
					// || (!String.valueOf(valoresConsulta[i])
					// .startsWith("%")
					// && !String.valueOf(valoresConsulta[i])
					// .endsWith("%") && String
					// .valueOf(valoresConsulta[i]).length() >= 3)) {
					// valoresConsulta[i] = String.valueOf(
					// valoresConsulta[i]).toUpperCase();
					// }
					// else {
					// validacion = true;
					// break;
					// }
					// }
					query.setParameter(String.valueOf(i + 1),
							valoresConsulta[i]);
				}
		if (validacion)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"PARA CUALQUIER BÚSQUEDA DEBE INGRESAR MAS DE 2 CARACTERES");
		else {
			list = query.list();
			if (list.isEmpty() && mensaje)
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRO NINGUNA COINCIDENCIA");
			else
				for (T t : list)
					for (int i = 0; i < valoresInicializar.length; i++) {
						try {
							Hibernate
									.initialize(t
											.getClass()
											.getMethod(
													valoresInicializar[i]
															.toString())
											.invoke(t));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public T obtenerObjeto(String consulta, Object[] valoresConsulta,
			boolean mensaje, Object[] valoresInicializar) {
		boolean validacion = false;
		List<T> list = null;
		Query query = (Query) session().createQuery(consulta);
		if (valoresConsulta != null)
			for (int i = 0; i < valoresConsulta.length; i++)
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
							valoresConsulta[i] = String.valueOf(
									valoresConsulta[i]).toUpperCase();
						} else {
							validacion = true;
							break;
						}
					}
					query.setParameter(String.valueOf(i + 1),
							valoresConsulta[i]);
				}
		if (validacion)
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"PARA CUALQUIER BÚSQUEDA DEBE INGRESAR MAS DE 2 CARACTERES");
		else {
			list = query.list();
			if (list.isEmpty() && mensaje)
				presentaMensaje(FacesMessage.SEVERITY_INFO,
						"NO SE ENCONTRO NINGUNA COINCIDENCIA");
			else if (!list.isEmpty()) {
				for (int i = 0; i < valoresInicializar.length; i++) {
					try {
						Hibernate.initialize(list.get(0).getClass()
								.getMethod(valoresInicializar[i].toString())
								.invoke(list.get(0)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return list.get(0);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<T> obtenerPorHql(String consulta, Object[] valores, int min,
			int max) {
		List<T> list = null;
		Query query = (Query) session().createQuery(consulta)
				.setFirstResult(min).setMaxResults(max);
		if (valores != null)
			for (int i = 0; i < valores.length; i++)
				query.setParameter(String.valueOf(i + 1), valores[i]);

		list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public T obtenerPorId(Class<T> type, K id) {
		return (T) session().get(type.getName(), id);
	}

	public Session session() {
		return sessionFactory.getCurrentSession();
	}
}