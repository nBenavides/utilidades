package ec.com.todocompu.utilidades.documentoelectronico.enumerador;
 
 
 public enum TipoAmbienteEnum
 {
	PRODUCCION("2"), PRUEBAS("1");

	private String code;

	private TipoAmbienteEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
 }
