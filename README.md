
CRUD de Productos Java

Esta es una API para gestionar productos, con nombre, precio, stock y categoria. Esta hecha con Java y Spring Boot, y guarda los datos en un archivo JSON en vez de una base de datos.

Estructura del proyecto

El proyecto esta organizado en carpetas: models tiene la clase Producto, services tiene la logica para guardar y leer los productos del archivo JSON, y controllers tiene los endpoints de la API. Tambien hay una carpeta static con una interfaz web simple para probar el CRUD desde el navegador.

Como instalarlo

Hay que tener instalado Java 17 o superior y Maven. Se clona el repositorio, se entra a la carpeta del proyecto, y se corre el comando mvn org.springframework.boot:spring-boot-maven-plugin:run. El servidor queda corriendo en localhost:8080. Ahi mismo, en la raiz, esta la interfaz web para crear, ver, editar y eliminar productos sin necesidad de usar Postman ni curl.

Endpoints

POST /productos crea un producto. GET /productos lista todos los productos. GET /productos/id trae un producto especifico. PUT /productos/id actualiza un producto. DELETE /productos/id elimina un producto.

Ejemplo de producto para probar

nombre: Cuaderno escolar, precio: 12000, stock: 25, categoria: Papeleria.

Validaciones

Al crear un producto, nombre, precio, stock y categoria son obligatorios. El precio y el stock no pueden ser negativos.

Dificultad

Easy pizi  lemos quezi