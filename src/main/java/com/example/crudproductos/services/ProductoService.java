package com.example.crudproductos.services;

import com.example.crudproductos.models.Producto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductoService {

    private final File archivo = new File("data/productos.json");
    private final ObjectMapper mapper = new ObjectMapper();
    private final AtomicLong contadorId = new AtomicLong(1);

    public ProductoService() {
        try {
            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs();
                mapper.writeValue(archivo, new ArrayList<Producto>());
            } else {
                List<Producto> existentes = leerProductos();
                long maxId = existentes.stream().mapToLong(Producto::getId).max().orElse(0);
                contadorId.set(maxId + 1);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el archivo de productos", e);
        }
    }

    private List<Producto> leerProductos() {
        try {
            CollectionType tipoLista = mapper.getTypeFactory()
                    .constructCollectionType(List.class, Producto.class);
            return mapper.readValue(archivo, tipoLista);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void guardarProductos(List<Producto> productos) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, productos);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo de productos", e);
        }
    }

    public List<Producto> listar() {
        return leerProductos();
    }

    public Optional<Producto> obtenerPorId(long id) {
        return leerProductos().stream().filter(p -> p.getId() == id).findFirst();
    }

    public Producto crear(Producto producto) {
        List<Producto> productos = leerProductos();
        producto.setId(contadorId.getAndIncrement());
        productos.add(producto);
        guardarProductos(productos);
        return producto;
    }

    public Optional<Producto> actualizar(long id, Producto datos) {
        List<Producto> productos = leerProductos();
        for (Producto p : productos) {
            if (p.getId() == id) {
                if (datos.getNombre() != null) p.setNombre(datos.getNombre());
                if (datos.getPrecio() != 0) p.setPrecio(datos.getPrecio());
                if (datos.getStock() != 0) p.setStock(datos.getStock());
                if (datos.getCategoria() != null) p.setCategoria(datos.getCategoria());
                guardarProductos(productos);
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public boolean eliminar(long id) {
        List<Producto> productos = leerProductos();
        boolean eliminado = productos.removeIf(p -> p.getId() == id);
        if (eliminado) {
            guardarProductos(productos);
        }
        return eliminado;
    }
}
