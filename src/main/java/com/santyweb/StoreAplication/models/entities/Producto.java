package com.santyweb.StoreAplication.models.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "producto")
public class Producto implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_producto")
	private Long idProducto;

	@Column(name = "nombre_producto")
	@NotEmpty
	private String nombreProducto;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "fecha_ingreso")
	@NotNull
	private Date fechaIngreso;

	@Column(name = "precio_compra_producto")
	@NotNull
	private Double precioCompraProducto;

	@Column(name = "precio_venta_producto")
	@NotNull
	private Double precioVentaProducto;

	@Column(name = "cantidad_stock")
	@NotNull
	private Long cantidadStock;
	
	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Double getPrecioCompraProducto() {
		return precioCompraProducto;
	}

	public void setPrecioCompraProducto(Double precioCompraProducto) {
		this.precioCompraProducto = precioCompraProducto;
	}

	public Double getPrecioVentaProducto() {
		return precioVentaProducto;
	}

	public void setPrecioVentaProducto(Double precioVentaProducto) {
		this.precioVentaProducto = precioVentaProducto;
	}

	public Long getCantidadStock() {
		return cantidadStock;
	}

	public void setCantidadStock(Long cantidadStock) {
		this.cantidadStock = cantidadStock;
	}
	
	private static final long serialVersionUID = 1L;

}
