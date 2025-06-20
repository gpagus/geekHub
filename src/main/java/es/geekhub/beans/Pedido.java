package es.geekhub.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Representa un pedido en la aplicación.
 * 
 * Un pedido incluye información como un identificador, la fecha de creación,
 * el estado del pedido (creado o finalizado), el usuario que realizó el pedido,
 * el importe total, el IVA, y una lista de líneas de pedido asociadas.
 * 
 * Implementa Serializable para permitir la transferencia y almacenamiento de objetos.
 * 
 * @author agp00
 */
public class Pedido implements Serializable {

    private Short idPedido;
    private Date fecha;
    private Estado estado;  // C: Creado, F: Finalizado
    private Usuario usuario;
    private double importe;
    private double iva;
    private List<LineaPedido> lineasPedidos;
    public enum Estado {
        c, f
    };

    public Short getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Short idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public List<LineaPedido> getLineasPedidos() {
        return lineasPedidos;
    }

    public void setLineasPedidos(List<LineaPedido> lineasPedidos) {
        this.lineasPedidos = lineasPedidos;
    }

}
