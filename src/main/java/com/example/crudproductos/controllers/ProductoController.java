package com.example.crudproductos.controllers;

import com.example.crudproductos.models.Producto;
import com.example.crudproductos.services.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producto producto) {
        String error = validar(producto, true);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        Producto creado = productoService.crear(producto);
        return ResponseEntity.status(201).body(creado);
    }

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable long id) {
        return productoService.obtenerPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Producto no encontrado")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable long id, @RequestBody Producto datos) {
        String error = validar(datos, false);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        return productoService.actualizar(id, datos)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "Producto no encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable long id) {
        boolean eliminado = productoService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.status(404).body(Map.of("error", "Producto no encontrado"));
        }
        return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado"));
    }

    private String validar(Producto producto, boolean esCreacion) {
        if (esCreacion) {
            if (producto.getNombre() == null || producto.getNombre().isBlank()
                    || producto.getCategoria() == null || producto.getCategoria().isBlank()) {
                return "Todos los campos son obligatorios: nombre, precio, stock, categoria";
            }
        }
        if (producto.getPrecio() < 0) {
            return "El precio no puede ser negativo";
        }
        if (producto.getStock() < 0) {
            return "El stock no puede ser negativo";
        }
        return null;
    }
}
