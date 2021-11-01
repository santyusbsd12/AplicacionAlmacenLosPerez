package com.santyweb.StoreAplication.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.santyweb.StoreAplication.models.entities.Producto;
import com.santyweb.StoreAplication.models.service.interfaces.IProductoService;

@Controller
@RequestMapping(path = "/Producto")
@SessionAttributes("producto")
public class ProductoController {

	@Autowired
	private IProductoService productoService;

	@RequestMapping(path = "/listar", method = RequestMethod.GET)
	public String findAllProducts(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Producto> productoPageable = productoService.findAllProductsPageable(pageRequest);

		model.addAttribute("titulo", "Gestión de productos");
		model.addAttribute("listadoProductos", productoPageable);
		return "GestionProducto";
	}

	@RequestMapping(path = "/formularioProducto", method = RequestMethod.GET)
	public String showFormProduct(Model model) {
		Producto producto = new Producto();

		model.addAttribute("producto", producto);
		model.addAttribute("titulo", "Agregar nuevo producto");
		return "AgregarProducto";
	}

	@RequestMapping(path = "/formularioProducto", method = RequestMethod.POST)
	public String saveProduct(@Valid Producto producto, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {

		String mensajeFlash = producto.getIdProducto() != null ? "Producto modificado con exito."
				: "Producto agregado con exito.";

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Agregar nuevo producto");
			return "AgregarProducto";
		}

		productoService.saveProduct(producto);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/Producto/listar";
	}

	@RequestMapping(path = "/actualizarProducto/{idProducto}", method = RequestMethod.GET)
	public String updateProduct(@PathVariable(value = "idProducto") Long idProducto, Model model,
			RedirectAttributes flash) {

		Producto producto = null;

		if (idProducto > 0) {
			producto = productoService.findById(idProducto);
			flash.addFlashAttribute("danger",
					"El id del producto no puede estar vacio, contacta al administrador para tratar el error.");
		} else {
			return "redirect:/Producto/listar";
		}

		model.addAttribute("producto", producto);
		model.addAttribute("titulo", "Editar producto");
		return "AgregarProducto";
	}

	@RequestMapping(path = "/eliminarProducto/{idProducto}", method = RequestMethod.GET)
	public String deleteProduct(@PathVariable(value = "idProducto") Long idProducto, Model model,
			RedirectAttributes flash) {

		if (idProducto > 0) {
			productoService.deleteById(idProducto);
			flash.addFlashAttribute("success", "Producto eliminado correctamente");
		} else {
			return "redirect:/Producto/listar";
		}

		return "redirect:/Producto/listar";
	}

	@RequestMapping(path = "/ImportarProductosMasivosFormulario", method = RequestMethod.GET)
	public String importMassiveProducts(Model model) {
		model.addAttribute("titulo", "Importar productos en masa");
		return "AgregarProductoMasivos";
	}

	@RequestMapping(path = "/ImportarProductosMasivosGuardar", method = RequestMethod.POST)
	public String processMassiveProducts(@RequestParam(name = "file") MultipartFile archivo, RedirectAttributes flash)
			throws IOException {

		List<Producto> productos = new ArrayList<Producto>();
		String nombreArchivo = archivo.getOriginalFilename();
		Path rutaArchivo = Paths.get("src//main//resources//static");
		String rutaArchivoString = rutaArchivo.toFile().getAbsolutePath();

		try {
			Path rutaFinal = Paths.get(rutaArchivoString + "//" + nombreArchivo);
			byte[] bytes = archivo.getBytes();
			Files.write(rutaFinal, bytes);

			InputStream file = new FileInputStream(rutaArchivoString + "//" + nombreArchivo);
 
			// 1- Aqui obtenemos el libro, es decir, le pasamos el archivo
			XSSFWorkbook libro = new XSSFWorkbook(file);

			// 2- Vamos a seleccionar la hoja del archivo
			XSSFSheet hoja = libro.getSheetAt(0);

			// 3- Obtenemos la primer fila para tener los nombres de los campos
			XSSFRow filaNombres = hoja.getRow(0);
			int totalFilas = hoja.getLastRowNum();

			String nombreCeldaString = "";
			for (int i = 1; i <= totalFilas; i++) {
				// 4- Obtenemos la fila y el numero total de columnas que posee
				XSSFRow fila = hoja.getRow(i);
				int numeroColumnas = fila.getPhysicalNumberOfCells();
				Producto producto = new Producto();

				for (int j = 0; j < numeroColumnas; j++) {
					XSSFCell valorCelda = fila.getCell(j);
					XSSFCell nombreCelda = filaNombres.getCell(j);
					nombreCeldaString = nombreCelda.getStringCellValue().toLowerCase();

					if (!"".equals(nombreCeldaString) || nombreCeldaString.length() > 0) {
						if (nombreCeldaString.equals("nombre del producto")) {
							producto.setNombreProducto(valorCelda.getStringCellValue());
						} else if (nombreCeldaString.equals("fecha de compra")) {
							if (HSSFDateUtil.isCellDateFormatted(valorCelda)) {
								producto.setFechaIngreso(valorCelda.getDateCellValue());
							} else {
								flash.addFlashAttribute("error", "Error al leer la fecha. Valida la fecha por favor.");
								return "redirect:/Producto/listar";
							}
						} else if (nombreCeldaString.equals("valor unitario de compra")) {
							producto.setPrecioCompraProducto(valorCelda.getNumericCellValue());
						} else if (nombreCeldaString.equals("valor unitario de venta")) {
							producto.setPrecioVentaProducto(valorCelda.getNumericCellValue());
						} else if (nombreCeldaString.equals("cantidad")) {
							Double valorDouble = valorCelda.getNumericCellValue();
							producto.setCantidadStock(valorDouble.longValue());
						}
					} else {
						flash.addFlashAttribute("error", "Ocurrió un problema con los nombres de las columnas,"
								+ " contacta con el administrador para tratar el error.");
						return "redirect:/Producto/listar";
					}
				}
				productos.add(producto);
			}

			productos.forEach(producto -> {
				System.out.println(producto);
			});

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			File archivoExiste = new File(rutaArchivo + "//" + nombreArchivo);
			Path rutaFinal = Paths.get(rutaArchivoString + "//" + nombreArchivo);
			if (!archivoExiste.exists()) {
				Files.delete(rutaFinal);
				System.out.println("OJO: ¡¡No existe el archivo de configuración!!");
			}
		}

		return "redirect:/Producto/listar";
	}
}
