# Informe técnico ReservaSport

ReservaSport es una aplicación Android para gestionar reservas de canchas deportivas. Permite crear, consultar, editar y eliminar reservas, mantener los datos localmente y asociar una imagen al tipo de cancha.



## Enfoque arquitectónico

El proyecto utiliza MVVM y separa datos, dominio, navegación e interfaz.





cl.duouc.reservasport.data

Reserva.kt

ReservaDao.kt

ReservaDatabase.kt

cl.duouc.reservasport.data.repository

ReservaRepository.kt





cl.duouc.reservasport.domain

ReservaValidator.kt

ImagenCanchaProvider.kt





cl.duouc.reservasport.navigation

AppNavigation.kt





cl.duouc.reservasport.ui

InicioScreen.kt

ReservaScreen.kt

ReservaViewModel.kt

ReservaViewModelFactory.kt





cl.duouc.reservasport

MainActivity.kt



### Flujo



View -> ViewModel -> Repository -> DAO -> Room



### Data

Contiene entidad Reserva, DAO, base de datos Room y repositorio.

ReservaRepository abstrae el acceso a datos y evita que el ViewModel dependa directamente del DAO.



### Domain

Contiene lógica independiente de Android.

ReservaValidator valida campos obligatorios.

ImagenCanchaProvider selecciona la URL de imagen según la cancha.



### UI

Contiene la interfaz desarrollada con Jetpack Compose y ReservaViewModel.

La UI observa el estado de la aplicación y envía eventos al ViewModel.



### Navigation

AppNavigation implementa un NavHost con las rutas inicio y reservas.

La navegación permite pasar desde la pantalla inicial hacia la pantalla de gestión de reservas y regresar mediante el botón Volver.



## Justificación de componentes Jetpack



ViewModel: Administra el estado de pantalla, coordina operaciones CRUD y separa la lógica de los composables.

StateFlow: Expone la lista de reservas como flujo observable y permite actualizar la interfaz cuando Room emite cambios.

Room: Implementa persistencia local mediante Reserva, ReservaDao, ReservaDatabase y ReservaRepository.

Navigation Compose: Administra la navegación entre la pantalla inicial y la pantalla de reservas.

Jetpack Compose: Construye la interfaz de la aplicación de forma declarativa.

Lifecycle y viewModelScope: Permite ejecutar corrutinas asociadas al ciclo de vida del ViewModel.



## Librerías y herramientas



Kotlin Coroutines, Coil Compose, LeakCanary en debug, JUnit, Compose UI Test, Espresso Core, AndroidJUnitRunner y Logcat.



## Pruebas unitarias



app/src/test/java/cl/duouc/reservasport/domain



### ReservaValidatorTest

Comprueba datos válidos, nombre vacío y cancha vacía.

Resultado observado: pruebas ejecutadas correctamente.



### ImagenCanchaProviderTest

Comprueba imagen de tenis, imagen de fútbol e imagen predeterminada.

Resultado observado: pruebas ejecutadas correctamente.



## Pruebas funcionales



app/src/androidTest/java/cl/duouc/reservasport/ReservaScreenTest.kt

### 

Comprueba que la pantalla inicial muestre ReservaSport y el botón Ver reservas.

Simula la navegación desde Inicio hacia Reservas.

Comprueba que se muestre la pantalla Reservas deportivas y el botón Agregar reserva.

Abre Nueva reserva.

Comprueba la presencia de los campos Nombre del usuario, Cancha, Fecha y Hora.

Presiona Guardar sin completar los campos.

Comprueba el mensaje Completa todos los campos antes de guardar.
Navega a Reservas, abre el formulario, ingresa datos y guarda nueva reserva.

Comprueba que la reserva aparezca en la lista.





## Herramientas de prueba



JUnit: Utilizado para validar lógica de dominio.

Compose UI Test: Utilizado para simular interacciones sobre interfaz desarrollada con Compose.

AndroidJUnitRunner: Utilizado para ejecutar pruebas en emulador o dispositivo.

Espresso Core: Incluido como dependencia de pruebas.

Logcat: Registra operaciones, errores y ejecución asincrónica.



## Evidencias de pruebas

https://drive.google.com/drive/folders/1v47EusJSI4m439HL\_eMpKI\_FYDwR9iX9?usp=sharing





## Configuración para publicación



applicationId: cl.duouc.reservasport  
minSdk: 24  
targetSdk: 36  
versionCode: 2  
versionName: 1.1



La aplicación utiliza permiso de internet para cargar imágenes remotas.

Se configuró nombre, ícono, versión y variante release.

El APK debe generarse firmado mediante Android Studio.

El keystore y las contraseñas no deben incluirse en el repositorio público.



## Recomendaciones



Reemplazar fallbackToDestructiveMigration por migraciones Room explícitas.

Agregar selectores de fecha y hora.

Validar formatos de fecha y hora.

Agregar confirmación antes de eliminar una reserva.

Incorporar autenticación de usuarios.

Evitar reservas duplicadas mediante validación de disponibilidad.

Agregar pruebas del Repository y ReservaViewModel.

Mantener respaldo seguro del keystore.

Implementar integración continua para ejecutar pruebas automáticamente.



## Reflexión final



La arquitectura MVVM permitió separar interfaz, estado y acceso a datos.

ReservaRepository redujo acoplamiento entre ReservaViewModel y Room.

La capa Domain permitió probar validaciones y selección de imágenes sin ejecutar Android.

Las pruebas unitarias comprobaron lógica de la app y las funcionales navegación, apertura de formularios, validación y registro de reservas.

