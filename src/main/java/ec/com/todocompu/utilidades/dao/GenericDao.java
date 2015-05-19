package ec.com.todocompu.utilidades.dao;

import java.io.Serializable;

import org.hibernate.Session;

public interface GenericDao<T, K extends Serializable> {

	void insertar(T t);

	void actualizar(T t);

	T obtenerObjeto(String consulta, Object[] valoresConsulta,
			Boolean presentarMensaje);

	Session session();
}