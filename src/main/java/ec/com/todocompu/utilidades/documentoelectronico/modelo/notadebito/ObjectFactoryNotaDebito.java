package ec.com.todocompu.utilidades.documentoelectronico.modelo.notadebito;
import javax.xml.bind.annotation.XmlRegistry;

import ec.com.todocompu.utilidades.documentoelectronico.modelo.InfoTributaria;
 
 @XmlRegistry
 public class ObjectFactoryNotaDebito
 {
   public NotaDebito.InfoAdicional createNotaDebitoInfoAdicional()
   {
     return new NotaDebito.InfoAdicional();
   }
 
   public ImpuestoNotaDebito createImpuesto()
   {
     return new ImpuestoNotaDebito();
   }
 
   public NotaDebito.Motivos createNotaDebitoMotivos()
   {
     return new NotaDebito.Motivos();
   }
 
   public NotaDebito.InfoNotaDebito.Impuestos createNotaDebitoInfoNotaDebitoImpuestos()
   {
     return new NotaDebito.InfoNotaDebito.Impuestos();
   }
 
   public NotaDebito.InfoNotaDebito createNotaDebitoInfoNotaDebito()
   {
     return new NotaDebito.InfoNotaDebito();
   }
 
   public NotaDebito.InfoAdicional.CampoAdicional createNotaDebitoInfoAdicionalCampoAdicional()
   {
     return new NotaDebito.InfoAdicional.CampoAdicional();
   }
 
   public Detalle createDetalle()
   {
     return new Detalle();
   }
 
   public InfoTributaria createInfoTributaria()
   {
     return new InfoTributaria();
   }
 
   public NotaDebito createNotaDebito()
   {
     return new NotaDebito();
   }
 
   public NotaDebito.Motivos.Motivo createNotaDebitoMotivosMotivo()
   {
     return new NotaDebito.Motivos.Motivo();
   }
 }