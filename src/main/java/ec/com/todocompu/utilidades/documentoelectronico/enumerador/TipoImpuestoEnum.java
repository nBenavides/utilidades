 package ec.com.todocompu.utilidades.documentoelectronico.enumerador;
 
 public enum TipoImpuestoEnum
 {
   RENTA(1, "Impuesto a la Renta"), IVA(2, "I.V.A."), ICE(3, "I.C.E.");
 
   private int code;
   private String descripcion;
 
   private TipoImpuestoEnum(int code, String descripcion) { this.code = code;
     this.descripcion = descripcion; }
 
   public String getDescripcion()
   {
     return this.descripcion;
   }
 
   public int getCode() {
     return this.code;
   }
 }

