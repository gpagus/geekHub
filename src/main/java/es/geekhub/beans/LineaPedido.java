package es.geekhub.beans;

import java.io.Serializable;

/**
 * Representa una línea individual dentro de un pedido.
 * 
 * Incluye el identificador de la línea, el pedido al que pertenece,
 * el producto asociado, y la cantidad solicitada.
 * 
 * Implementa Serializable para permitir la transferencia de objetos.
 * 
 * @author agp00
 */
public class LineaPedido implements Serializable {
    
    private Short idLinea;
    private Pedido pedido;
    private Producto producto;
    private Byte cantidad;

    public Short getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Short idLinea) {
        this.idLinea = idLinea;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Byte getCantidad() {
        return cantidad;
    }

    public void setCantidad(Byte cantidad) {
        this.cantidad = cantidad;
    }

}
