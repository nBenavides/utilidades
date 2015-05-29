package ec.com.todocompu.utilidades.enumerador;

public enum Mes {

	M00("00", "MES"), 
	M01("01", "ENERO"), 
	M02("02", "FEBRERO"), 
	M03("03", "MARZO"), 
	M04("04", "ABRIL"), 
	M05("05", "MAYO"), 
	M06("06", "JUNIO"), 
	M07("07", "JULIO"),
	M08("08", "AGOSTO"),
	M09("09", "SEPTIEMBRE"),	
	M10("10", "OCTUBRE"),
	M11("11", "NOVIEMBRE"),	
	M12("12", "DICIEMBRE");	
	
	private final String id;
	private final String nombre;

	private Mes(String id, String nombre) {
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
