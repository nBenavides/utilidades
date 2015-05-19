package ec.com.todocompu.utilidades.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface GenericDao<T, K extends Serializable> {

	boolean actualizar(T t);

	Boolean comprobarIndiceUnico(T t, String atributo, String valor);

	Object contar(Class<T> type);

	// Object contar(String consulta, Object[] valores);

	// void evict(T t);

	void insertar(T t);

	T obtenerPorAtributo(String entidad, String atributo, String valorAtributo);

	List<T> obtenerLista(String consulta, Object[] valoresConsulta,
			boolean mensaje, Object[] valoresInicializar);

	T obtenerObjeto(String consulta, Object[] valoresConsulta, boolean mensaje,
			Object[] valoresInicializar);

	List<T> obtenerPorHql(String consulta, Object[] valores, int min, int max);

	T obtenerPorId(Class<T> type, K id);

	Session session();
}