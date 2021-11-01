package com.santyweb.StoreAplication.models.dao.interfaces;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.santyweb.StoreAplication.models.entities.Producto;

public interface IProductoDAO extends PagingAndSortingRepository<Producto, Long>{

	// Usando metodos de la interfaz CrudRepository propia de spring

}
