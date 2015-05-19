package ec.com.todocompu.utilidades.enums;

public enum Meses {
	
	M01(1, "ENERO"), 
	M02(2, "FEBRERO"), 
	M03(3, "MARZO"), 
	M04(4, "ABRIL"), 
	M05(5, "MAYO"), 
	M06(6, "JUNIO"), 
	M07(7, "JULIO"),
	M08(8, "AGOSTO"),
	M09(9, "SEPTIEMBRE"),	
	M10(10, "OCTUBRE"),
	M11(11, "NOVIEMBRE"),	
	M12(12, "DICIEMBRE");	
	
	private final Integer id;
	private final String nombre;

	private Meses(Integer id, String nombre) {
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
