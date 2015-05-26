package ec.com.todocompu.utilidades.documentoelectronico.enumerador;
 
 
 public enum TipoCompradorEnum
 {
   CONSUMIDOR_FINAL("07"), RUC("04"), CEDULA("05"), PASAPORTE("06");
 
   private String code;
 
   private TipoCompradorEnum(String code) { this.code = code;
   }
 
   public String getCode()
   {
     return this.code;
   }
 
   public static String retornaCodigo(String valor)
   {
     String codigo = null;
 
     if (valor.equals("C")) {
       codigo = CEDULA.getCode();
     }
 
     if (valor.equals("R")) {
       codigo = RUC.getCode();
     }
 
     if (valor.equals("P")) {
       codigo = PASAPORTE.getCode();
     }
 
     if (valor.equals("F")) {
       codigo = CONSUMIDOR_FINAL.getCode();
     }
 
     return codigo;
   }
 }
