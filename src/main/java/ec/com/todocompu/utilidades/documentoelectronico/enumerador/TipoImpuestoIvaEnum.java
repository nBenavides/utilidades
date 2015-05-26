 package ec.com.todocompu.utilidades.documentoelectronico.enumerador;
 
 public enum TipoImpuestoIvaEnum
 {
   IVA_VENTA_0("0"), IVA_VENTA_12("2"), IVA_VENTA_EXCENTO("6");
 
   private String code;
 
   private TipoImpuestoIvaEnum(String code) { this.code = code;
   }
 
   public String getCode()
   {
     return this.code;
   }
 }

