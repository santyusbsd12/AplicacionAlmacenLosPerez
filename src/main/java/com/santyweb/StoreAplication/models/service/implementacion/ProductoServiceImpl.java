package com.santyweb.StoreAplication.models.service.implementacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.santyweb.StoreAplication.models.dao.interfaces.IProductoDAO;
import com.santyweb.StoreAplication.models.entities.Producto;
import com.santyweb.StoreAplication.models.service.interfaces.IProductoService;

@Service
public class ProductoServiceImpl implements IProductoService {

	@Autowired
	private IProductoDAO productoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAllProducts() {
		return (List<Producto>) productoDao.findAll();
	}

	@Override
	@Transactional
	public void saveProduct(Producto producto) {
		productoDao.save(producto);
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long idProducto) {
		return productoDao.findById(idProducto).orElse(null);
	}

	@Override
	@Transactional
	public void deleteById(Long idProducto) {
		productoDao.deleteById(idProducto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAllProductsPageable(Pageable page) {
		return productoDao.findAll(page);
	}
}