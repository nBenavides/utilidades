package ec.com.todocompu.utilidades.documentoelectronico.modelo.reporte;
 
import java.util.ArrayList;
import java.util.List;

import ec.com.todocompu.utilidades.documentoelectronico.modelo.notadebito.NotaDebito;
 
 public class NotaDebitoReporte
 {
   private NotaDebito notaDebito;
   private List<DetallesAdicionalesReporte> detallesAdiciones;
   private List<InformacionAdicional> infoAdicional;
 
   public NotaDebitoReporte(NotaDebito notaDebito)
   {
     this.notaDebito = notaDebito;
   }
 
   public List<DetallesAdicionalesReporte> getDetallesAdiciones()
   {
     this.detallesAdiciones = new ArrayList<DetallesAdicionalesReporte>();
     for (NotaDebito.Motivos.Motivo motivo : this.notaDebito.getMotivos().getMotivo()) {
       DetallesAdicionalesReporte detAd = new DetallesAdicionalesReporte();
       detAd.setRazonModificacion(motivo.getRazon());
       detAd.setValorModificacion(motivo.getValor().toString());
       detAd.setInfoAdicional(getInfoAdicional());
       this.detallesAdiciones.add(detAd);
     }
     return this.detallesAdiciones;
   }
 
   public void setDetallesAdiciones(List<DetallesAdicionalesReporte> detallesAdiciones)
   {
     this.detallesAdiciones = detallesAdiciones;
   }
 
   public NotaDebito getNotaDebito()
   {
     return this.notaDebito;
   }
 
   public void setNotaDebito(NotaDebito notaDebito)
   {
     this.notaDebito = notaDebito;
   }
 
   public List<InformacionAdicional> getInfoAdicional()
   {
     if ((this.notaDebito.getInfoAdicional() != null) && (!this.notaDebito.getInfoAdicional().getCampoAdicional().isEmpty())) {
			this.infoAdicional = new ArrayList<InformacionAdicional>();
       for (NotaDebito.InfoAdicional.CampoAdicional info : this.notaDebito.getInfoAdicional().getCampoAdicional()) {
         InformacionAdicional ia = new InformacionAdicional(info.getValue(), info.getNombre());
         this.infoAdicional.add(ia);
       }
     }
     return this.infoAdicional;
   }
 
   public void setInfoAdicional(List<InformacionAdicional> infoAdicional)
   {
     this.infoAdicional = infoAdicional;
   }
 }

