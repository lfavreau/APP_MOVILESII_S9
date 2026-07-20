# ReservaSport

Aplicación Android para gestionar reservas de canchas deportivas. Permite crear, consultar, editar y eliminar reservas, mantener los datos localmente, asociar una imagen al tipo de cancha, consultar el clima de Concepción, recibir alertas climáticas y conservar una sesión de usuario mediante almacenamiento local.



## Funcionalidades

Pantalla de inicio de sesión.

Validación de correo electrónico y contraseña.

Persistencia de sesión con SharedPreferences.

Recuperación simulada de contraseña.

Inicio automático cuando existe una sesión activa.

Cierre de sesión.

Pantalla inicial con información del usuario.

Navegación entre Login, Inicio y Reservas.

CRUD de reservas.

Persistencia local con Room.

Validación de campos obligatorios.

Procesamiento asincrónico con Kotlin Coroutines.

Carga de imágenes remotas con Coil.

Consumo de datos climáticos con Retrofit.

Consulta de temperatura, humedad, lluvia, precipitación y viento.

Consulta de temperatura máxima y mínima.

Consulta de probabilidad de lluvia.

Recomendaciones para actividades deportivas según las condiciones climáticas.

Actualización manual del clima.

Manejo de estados de carga, mensajes y errores.

Notificaciones automáticas por lluvia o viento fuerte.

Botón para simular una alerta de lluvia.

Detalle de reservas mediante Modal Bottom Sheet.

Pruebas unitarias y funcionales automatizadas.



## Arquitectura implementada

El proyecto utiliza MVVM y separa datos, dominio, sesión, consumo remoto, notificaciones, navegación e interfaz.


cl.duouc.reservasport.data  
Reserva.kt  
ReservaDao.kt  
ReservaDatabase.kt  


cl.duouc.reservasport.data.repository  
ReservaRepository.kt  
WeatherRepository.kt  


cl.duouc.reservasport.data.remote  
RetrofitClient.kt  
WeatherApiService.kt  
WeatherResponse.kt  


cl.duouc.reservasport.data.session  
SessionManager.kt  


cl.duouc.reservasport.domain  
ReservaValidator.kt  
ImagenCanchaProvider.kt  


cl.duouc.reservasport.navigation  
AppNavigation.kt  


cl.duouc.reservasport.notification  
NotificationHelper.kt  


cl.duouc.reservasport.ui  
LoginScreen.kt  
InicioScreen.kt  
ReservaScreen.kt  
ReservaViewModel.kt  
ReservaViewModelFactory.kt  
WeatherViewModel.kt  
WeatherViewModelFactory.kt  


cl.duouc.reservasport  
MainActivity.kt  



### Flujo local de reservas


View -> ViewModel -> Repository -> DAO -> Room



### Flujo de clima


View -> WeatherViewModel -> WeatherRepository -> Retrofit -> Open-Meteo



### Flujo de sesión


LoginScreen -> SessionManager -> SharedPreferences -> AppNavigation



### Flujo de notificaciones


Open-Meteo -> WeatherViewModel -> NotificationHelper -> Notificación Android



### Data

Contiene entidad Reserva, DAO, base de datos Room y repositorios.

ReservaRepository abstrae acceso a datos y evita que el ViewModel dependa directamente del DAO.

WeatherRepository abstrae el consumo del servicio climático y evita que WeatherViewModel dependa directamente de Retrofit.



### Remote

Contiene la configuración y modelos necesarios para consumir Open-Meteo.

RetrofitClient configura la URL base, Gson y la instancia de WeatherApiService.

WeatherApiService define las consultas HTTP hacia la API.

WeatherResponse representa la respuesta JSON recibida desde Open-Meteo.



### Session

SessionManager administra la sesión mediante SharedPreferences.

Permite guardar sesión, consultar si existe una sesión activa, recuperar el correo y cerrar sesión.



### Domain

Lógica independiente de Android:

ReservaValidator: valida campos obligatorios.

ImagenCanchaProvider: selecciona la URL de imagen según la cancha.



### Notification

NotificationHelper crea el canal de notificaciones y muestra alertas climáticas.

Comprueba el permiso POST_NOTIFICATIONS antes de enviar una notificación.



### UI

Contiene Compose y ViewModel.

LoginScreen administra el formulario de acceso y recuperación de contraseña.

InicioScreen muestra usuario, clima, recomendaciones y accesos principales.

ReservaScreen muestra reservas, formulario CRUD y detalle mediante Bottom Sheet.

ReservaViewModel administra reservas y operaciones con Room.

WeatherViewModel administra datos climáticos, estados, errores, recomendaciones y alertas.



### Navigation

AppNavigation implementa un NavHost con las rutas login, inicio y reservas.

La ruta inicial depende de la existencia de una sesión activa.

Cuando existe una sesión guardada, la aplicación abre directamente Inicio.

Cuando no existe sesión, la aplicación muestra Login.

Cerrar sesión limpia SharedPreferences y regresa al Login.



## Componentes Jetpack utilizados:



ViewModel: Administra estado de pantalla, coordina operaciones CRUD, clima y notificaciones, y separa lógica del compose.

StateFlow: Expone la lista de reservas como flujo observable y permite actualizar la interfaz cuando Room emite cambios.

Room: Implementa persistencia local mediante Reserva, ReservaDao, ReservaDatabase y ReservaRepository.

Navigation Compose: Administra la navegación entre Login, Inicio y Reservas.

Jetpack Compose: Construye la interfaz de la aplicación.

Material 3: Proporciona componentes visuales, estilos, botones, tarjetas, diálogos y Bottom Sheet.

Lifecycle y viewModelScope: Permite ejecución de corrutinas asociadas al ciclo de vida del ViewModel.

SharedPreferences: Mantiene la sesión local del usuario.

ModalBottomSheet: Muestra el detalle de una reserva y las acciones Editar, Eliminar y Cerrar.



## Librerías y herramientas



Kotlin Coroutines, Coil Compose, Retrofit, Gson Converter, Room, Navigation Compose, SharedPreferences, Material 3, Modal Bottom Sheet, NotificationCompat, LeakCanary en debug, JUnit, Compose UI Test, Espresso Core, AndroidJUnitRunner, Logcat



## Autenticación local



La aplicación utiliza autenticación local con persistencia de sesión mediante SharedPreferences.



Credenciales de demostración:


Correo: demo@reservasport.cl  
Contraseña: Reserva123  



El sistema valida formato de correo, contraseña, mensajes de error y recuperación simulada.

Después de iniciar sesión correctamente, la aplicación guarda el correo y el estado de autenticación.

Al cerrar y volver a abrir la aplicación, la sesión permanece activa.

Cerrar sesión elimina los datos guardados y devuelve al Login.



## Consumo de API climática



La aplicación consume Open-Meteo mediante Retrofit.



URL base:


https://api.open-meteo.com/



Coordenadas utilizadas para Concepción obtenidas en la web de Open-Meteo:


latitude: -36.827  
longitude: -73.0498  



Datos solicitados sacados del constructor interactivo de la API de la web:

Temperatura actual.

Humedad relativa.

Precipitación actual.

Lluvia actual.

Viento actual.

Temperatura máxima.

Temperatura mínima.

Lluvia total.

Precipitación total.

Horas de precipitación.

Probabilidad máxima de lluvia.

Viento máximo.



## Recomendaciones climáticas



WeatherViewModel procesa la información recibida y genera recomendaciones.



Alta probabilidad de lluvia o precipitación importante:

Se recomienda reservar una cancha cubierta.



Viento fuerte:

Se recomienda realizar la actividad en interior.



Probabilidad intermedia de lluvia:

Se recomienda revisar nuevamente el clima antes de salir.



Condiciones normales:

Se informa que existen condiciones adecuadas para actividades deportivas.



## Notificaciones



La aplicación crea un canal llamado Alertas climáticas.

Puede enviar una notificación automática cuando:

La probabilidad de lluvia es igual o superior a 70%.

La precipitación total es igual o superior a 5 mm.

El viento máximo es igual o superior a 40 km/h.



También incluye el botón Probar alerta de lluvia que simula una condición de lluvia y permite comprobar manualmente el funcionamiento de las notificaciones.



## Bottom Sheet



Las tarjetas de reserva permiten abrir un Modal Bottom Sheet.

El detalle muestra:

Nombre del usuario.

Cancha.

Fecha.

Hora.

Estado.

Imagen.

Botón Editar reserva.

Botón Eliminar reserva.

Botón Cerrar.


## Pruebas unitarias


app/src/test/java/cl/duouc/reservasport/domain


### ReservaValidatorTest


Comprueba datos válidos, nombre vacío y cancha vacía.


### ImagenCanchaProviderTest


Comprueba imagen de tenis, imagen de fútbol e imagen predeterminada.


## Ejecutar el proyecto

### Requisitos



* Android Studio
* JDK 11
* Android SDK 24 o superior
* Conexión a internet para Coil y Open-Meteo
* Emulador o dispositivo Android
* Permiso de notificaciones en Android 13 o superior



### Pasos

1. Clonar el repositorio.
2. Abrir el proyecto en Android Studio.
3. Sincronizar Gradle.
4. Seleccionar emulador o dispositivo.
5. Ejecutar el módulo app.
6. Aceptar el permiso de notificaciones.
7. Iniciar sesión con las credenciales de demostración.


git clone https://github.com/lfavreau/APP_MOVILESII_S9.git  
cd ReservaSport


## Ejecutar pruebas unitarias JUnit


./gradlew testDebugUnitTest


## Ejecutar pruebas funcionales


./gradlew connectedDebugAndroidTest


## Permisos


INTERNET: Permite cargar imágenes con Coil y consultar Open-Meteo mediante Retrofit.

POST_NOTIFICATIONS: Permite mostrar alertas climáticas en Android 13 o superior.

## Autor

Luciano Favreau
