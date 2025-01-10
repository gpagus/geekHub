
package es.geekhub.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author agp00
 */
public class Filtros implements Serializable {
    private List<Byte> categorias; // IDs de las categorías seleccionadas
    private List<String> marcas;   // Marcas seleccionadas
    private String priceRange;     // Mantén el rango de precio como está

    public List<Byte> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Byte> categorias) {
        this.categorias = categorias;
    }

    public List<String> getMarcas() {
        return marcas;
    }

    public void setMarcas(List<String> marcas) {
        this.marcas = marcas;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    

    // Métodos auxiliares para obtener valores mínimos y máximos del rango de precios
    public Double getPrecioMin() {
        if (priceRange != null && priceRange.contains("-")) {
            return Double.parseDouble(priceRange.split("-")[0]);
        }
        return null;
    }

    public Double getPrecioMax() {
        if (priceRange != null && priceRange.contains("-")) {
            return Double.parseDouble(priceRange.split("-")[1]);
        }
        return null;
    }
}
