# Sistema de Carrito de Compras - Walmart Latam

## Información del Proyecto

**Universidad:** Universidad Don Bosco  
**Materia:** Desarrollo de Software para Móviles (DSM941)  
**Proyecto:** Proyecto 1 de Cátedra  
**Grupo:** #3  
**Estudiante:** Isidro Alexander Marroquín Echeverría  
**Correo:** isidro.marroquin@udb.edu.sv  
**Carne:** ME221443  
**Fecha:** Marzo 2026

## Descripción

Sistema de gestión de compras en línea desarrollado en Kotlin que simula un carrito de compras para Walmart Latam. El sistema permite a los clientes navegar por un catálogo de productos, agregar artículos al carrito, gestionar sus compras y generar facturas en múltiples formatos (TXT, HTML, PDF).

El proyecto implementa principios de programación orientada a objetos, manejo de excepciones, persistencia de datos en JSON, generación de documentos y logging de operaciones.

## Características Principales

- Registro de clientes con validación de datos
- Catálogo de productos con gestión de inventario en tiempo real
- Carrito de compras con validación de stock disponible
- Edición de carrito (agregar/eliminar productos)
- Cálculo de stock real considerando productos en carrito
- Proceso de checkout con confirmación
- Generación de facturas en tres formatos: TXT, HTML y PDF
- Envío de facturas por correo electrónico (configurable)
- Persistencia de inventario y historial de compras en JSON
- Sistema de logging para auditoría de operaciones
- Interfaz de consola con tablas ASCII formateadas

## Tecnologías Utilizadas

### Lenguaje y Plataforma
- **Kotlin 2.0.0** - Lenguaje de programación principal
- **JVM 21** - Plataforma de ejecución
- **Gradle 8.10.1** - Sistema de construcción y gestión de dependencias

### Bibliotecas y Dependencias

#### Generación de Tablas
- **AsciiTable 0.3.2** - Renderizado de tablas en consola

#### Persistencia de Datos
- **Gson 2.10.1** - Serialización/deserialización JSON para inventario e historial

#### Manejo de Fechas
- **Kotlinx DateTime 0.6.0** - Gestión de fechas y timestamps

#### Generación de Documentos
- **iText7 Core 7.2.5** - Generación de facturas en formato PDF

#### Envío de Correos
- **JavaMail 1.6.2** - Envío de facturas por correo electrónico

#### Testing
- **JUnit 5** - Framework de pruebas unitarias
- **Kotlin Test** - Extensiones de testing para Kotlin

#### Herramientas de Construcción
- **Shadow Plugin 7.1.2** - Empaquetado de JAR con dependencias

## Estructura del Proyecto

```
proyecto_1/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   └── kotlin/
│   │   │       └── org/
│   │   │           └── example/
│   │   │               ├── App.kt                    # Punto de entrada y flujo principal
│   │   │               ├── Carrito.kt                # Gestión del carrito de compras
│   │   │               ├── Cliente.kt                # Modelo de datos del cliente
│   │   │               ├── Excepciones.kt            # Excepciones personalizadas
│   │   │               ├── GeneradorFactura.kt       # Generación de facturas TXT/HTML
│   │   │               ├── GeneradorPDF.kt           # Generación de facturas PDF
│   │   │               ├── GestorHistorial.kt        # Persistencia de historial de compras
│   │   │               ├── GestorInventario.kt       # Persistencia de inventario
│   │   │               ├── Logger.kt                 # Sistema de logging
│   │   │               ├── Menu.kt                   # Utilidades de menú (legacy)
│   │   │               ├── Producto.kt               # Modelo de producto
│   │   │               ├── ProductoCarrito.kt        # Producto en carrito
│   │   │               ├── ProductoInventario.kt     # Producto con stock
│   │   │               ├── ServicioCorreo.kt         # Envío de correos
│   │   │               ├── Utilidades.kt             # Funciones auxiliares
│   │   │               └── Validador.kt              # Validaciones de entrada
│   │   └── test/
│   │       └── kotlin/
│   │           └── org/
│   │               └── example/
│   │                   └── AppTest.kt                # Pruebas unitarias
│   ├── build.gradle.kts                              # Configuración de Gradle del módulo
│   └── logs/
│       └── app.log                                   # Log de la aplicación
├── config/
│   └── mail.properties                               # Configuración de correo (opcional)
├── data/
│   ├── inventario.json                               # Persistencia del inventario
│   └── historial_compras.json                        # Historial de transacciones
├── facturas/                                         # Facturas generadas (TXT/HTML/PDF)
├── logs/
│   └── app.log                                       # Log del sistema
├── gradle/
│   ├── wrapper/                                      # Gradle Wrapper
│   └── libs.versions.toml                            # Catálogo de versiones
├── settings.gradle.kts                               # Configuración de Gradle del proyecto
├── gradlew                                           # Script de Gradle para Unix/Linux
├── gradlew.bat                                       # Script de Gradle para Windows
├── Diagrama.js                                       # Diagrama de flujo en Mermaid.js
└── README.md                                         # Este archivo
```

### Descripción de Componentes Principales

#### Módulo Core (org.example)

**App.kt**  
Punto de entrada del sistema. Contiene el flujo principal de la aplicación: inicialización, captura de cliente, navegación de catálogo, gestión de carrito, checkout y procesamiento de compras.

**Cliente.kt**  
Modelo de datos que representa un cliente con nombre y correo electrónico. Incluye validaciones y método para obtener nombre formal.

**Carrito.kt**  
Gestiona la lógica del carrito de compras: agregar productos, eliminar productos, calcular totales y mostrar contenido en formato tabla.

**Producto.kt / ProductoCarrito.kt / ProductoInventario.kt**  
Modelos de datos para representar productos en diferentes contextos: catálogo base, productos en carrito y productos con stock en inventario.

**GestorInventario.kt**  
Maneja la persistencia del inventario en formato JSON. Permite cargar, guardar y actualizar el stock de productos.

**GestorHistorial.kt**  
Registra el historial de compras en formato JSON para auditoría y análisis posterior.

**GeneradorFactura.kt**  
Genera facturas en formato texto plano y HTML con información detallada de la compra.

**GeneradorPDF.kt**  
Utiliza iText7 para generar facturas profesionales en formato PDF con formato y estructura empresarial.

**ServicioCorreo.kt**  
Gestiona el envío de facturas por correo electrónico usando JavaMail. Configurable mediante archivo properties.

**Logger.kt**  
Sistema de logging que registra operaciones, errores y eventos del sistema en archivo de log con niveles INFO, DEBUG, ERROR.

**Validador.kt**  
Contiene validaciones de entrada: formato de email, cantidades, strings no vacíos, etc.

**Excepciones.kt**  
Define excepciones personalizadas para manejo de errores específicos del dominio: stock insuficiente, producto no encontrado, email inválido, etc.

## Requisitos del Sistema

- Java Development Kit (JDK) 21 o superior
- Gradle 8.10.1 o superior (incluido via Gradle Wrapper)
- Sistema operativo: Windows, Linux o macOS
- Mínimo 512 MB de RAM
- 100 MB de espacio en disco

## Instalación

### 1. Clonar o descargar el proyecto

```bash
git clone <https://github.com/marroquin9953/Proyecto-1---DSM941.git>
cd proyecto_1
```

### 2. Verificar instalación de Java

```bash
java -version
```

Debe mostrar Java 21 o superior.

### 3. Dar permisos de ejecución al Gradle Wrapper (Linux/macOS)

```bash
chmod +x gradlew
```

## Compilación

### Compilar el proyecto

```bash
./gradlew build
```

En Windows:
```cmd
gradlew.bat build
```

### Generar JAR ejecutable con dependencias

```bash
./gradlew shadowJar
```

El JAR se generará en: `app/build/libs/app-all.jar`

## Ejecución

### Opción 1: Ejecutar con Gradle

```bash
./gradlew run
```

En Windows:
```cmd
gradlew.bat run
```

### Opción 2: Ejecutar JAR directamente

```bash
java -jar app/build/libs/app-all.jar
```

### Opción 3: Desde IDE

Abrir el proyecto en IntelliJ IDEA o Android Studio y ejecutar la clase `AppKt` (función `main`).

## Uso del Sistema

### 1. Registro de Cliente

Al iniciar, el sistema solicita:
- Nombre completo
- Correo electrónico (con validación de formato)

### 2. Navegación del Catálogo

El sistema muestra una tabla con:
- ID del producto
- Nombre del producto
- Precio unitario
- Stock disponible real (considerando productos ya en carrito)

### 3. Agregar Productos al Carrito

- Ingresar el ID del producto deseado
- Especificar la cantidad (validada contra stock disponible)
- El sistema actualiza el carrito y muestra el resumen

### 4. Opciones Post-Agregar

Después de agregar un producto:
1. Continuar comprando
2. Editar carrito (eliminar productos)
3. Finalizar compra

### 5. Editar Carrito

- Ver productos en carrito
- Seleccionar ID del producto a eliminar
- Especificar cantidad a eliminar
- Si el carrito queda vacío, regresa al catálogo

### 6. Checkout

- Revisar resumen completo de la compra
- Confirmar o cancelar la transacción

### 7. Procesamiento de Compra

Si se confirma:
- Actualiza el inventario
- Genera factura en TXT, HTML y PDF
- Intenta enviar factura por correo (si está configurado)
- Guarda el historial de compra
- Limpia el carrito

### 8. Nueva Compra

El sistema pregunta si desea realizar otra compra o finalizar.

## Configuración de Correo Electrónico (Opcional)

Para habilitar el envío de facturas por correo:

1. Editar el archivo `config/mail.properties`:

```properties
mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.username=tu-correo@gmail.com
mail.password=tu-contraseña-de-aplicacion
mail.from=tu-correo@gmail.com
```

2. Para Gmail, generar una contraseña de aplicación en:
   https://myaccount.google.com/apppasswords

Si no se configura, las facturas se guardan localmente en la carpeta `facturas/`.

## Archivos Generados

### Inventario (data/inventario.json)
Almacena el estado actual del inventario con stock actualizado.

### Historial (data/historial_compras.json)
Registra todas las compras realizadas con detalles completos.

### Facturas (facturas/)
- `FAC-YYYYMMDD_HHMMSS.txt` - Factura en texto plano
- `FAC-YYYYMMDD_HHMMSS.html` - Factura en HTML
- `FAC-YYYYMMDD_HHMMSS.pdf` - Factura en PDF

### Logs (logs/app.log)
Registro de todas las operaciones del sistema con timestamps.

## Pruebas

### Ejecutar pruebas unitarias

```bash
./gradlew test
```

### Ver reporte de pruebas

El reporte HTML se genera en: `app/build/reports/tests/test/index.html`

## Principios de Diseño Aplicados

### SOLID
- **Single Responsibility**: Cada clase tiene una responsabilidad única (GestorInventario, GeneradorFactura, etc.)
- **Open/Closed**: Extensible mediante herencia y composición
- **Liskov Substitution**: Modelos de datos intercambiables donde corresponde
- **Interface Segregation**: Interfaces específicas para cada funcionalidad
- **Dependency Inversion**: Dependencias inyectadas y configurables

### Patrones de Diseño
- **Repository Pattern**: GestorInventario, GestorHistorial
- **Factory Pattern**: Creación de inventario inicial
- **Builder Pattern**: Generación de facturas
- **Singleton Pattern**: Logger

### Buenas Prácticas
- Manejo robusto de excepciones
- Validación de entrada de usuario
- Logging de operaciones críticas
- Separación de responsabilidades
- Código limpio y legible
- Documentación inline

## Limitaciones Conocidas

- El sistema es de consola, no tiene interfaz gráfica
- El envío de correo requiere configuración manual
- No hay autenticación de usuarios
- El inventario es local, no hay base de datos
- No hay soporte para múltiples monedas
- No hay sistema de descuentos o promociones

## Posibles Mejoras Futuras

- Interfaz gráfica con JavaFX o Compose Desktop
- Base de datos relacional (PostgreSQL, MySQL)
- Autenticación y autorización de usuarios
- Sistema de roles (cliente, administrador)
- Reportes y estadísticas de ventas
- Integración con pasarelas de pago
- Soporte multiidioma
- API REST para integración con otros sistemas
- Notificaciones push
- Sistema de recomendaciones

## Solución de Problemas

### Error: "Java version not found"
Instalar JDK 21 o superior y configurar JAVA_HOME.

### Error: "Permission denied" al ejecutar gradlew
Ejecutar: `chmod +x gradlew`

### Error al generar PDF
Verificar que iText7 esté correctamente incluido en las dependencias.

### Error al enviar correo
Verificar configuración en `config/mail.properties` y credenciales de correo.

### Inventario no se guarda
Verificar permisos de escritura en la carpeta `data/`.

## Contacto

**Estudiante:** Isidro Alexander Marroquín Echeverría  
**Correo:** isidro.marroquin@udb.edu.sv  
**Carne:** ME221443  
**Grupo:** #3  
**Universidad:** Universidad Don Bosco  
**Materia:** DSM941 - Desarrollo de Software para Móviles

## Licencia

Este proyecto es de uso académico para la Universidad Don Bosco.
