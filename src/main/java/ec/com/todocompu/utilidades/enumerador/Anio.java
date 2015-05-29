package ec.com.todocompu.utilidades.enumerador;

public enum Anio {

	A0000("0000", "AÑO"), 
	A2014("2014", "2014"), 
	A2015("2015", "2015"),
	A2016("2016", "2016");

	private final String id;
	private final String nombre;

	private Anio(String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
}
