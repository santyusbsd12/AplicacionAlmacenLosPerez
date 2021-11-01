package com.santyweb.StoreAplication.models.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.santyweb.StoreAplication.models.entities.Producto;

public interface IProductoService {

	public List<Producto> findAllProducts();
	
	public Page<Producto> findAllProductsPageable(Pageable page);
	
	public void saveProduct(Producto producto);
	
	public Producto findById(Long idProducto);
	
	public void deleteById(Long idProducto);

}
