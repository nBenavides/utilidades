package ec.com.todocompu.utilidades.enumerador;

public enum Anio {
	
	A2013(2013, "2013"), 
	A2014(2014, "2014"), 
	A2015(2015, "2015");

	private final Integer id;
	private final String nombre;

	private Anio(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
}
