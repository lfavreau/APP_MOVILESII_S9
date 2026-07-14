# ReservaSport

Aplicación Android para gestionar reservas de canchas deportivas. Permite crear, consultar, editar y eliminar reservas, mantener los datos localmente y asociar una imagen al tipo de cancha.



## Funcionalidades

Pantalla inicial.

Navegación entre Inicio y Reservas.

CRUD de reservas.

Persistencia local con Room.

Validación de campos obligatorios.

Procesamiento asincrónico con Kotlin Coroutines.

Carga de imágenes remotas con Coil.

Manejo de estados de carga, mensajes y errores.

Pruebas unitarias y funcionales automatizadas.



## Arquitectura implementada

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



Contiene entidad Reserva, DAO, base de datos Room y repositorio. ReservaRepository abstrae acceso a datos y evita que el ViewModel dependa directamente del DAO.



### Domain

Lógica independiente de Android:

ReservaValidator: valida campos obligatorios.

ImagenCanchaProvider: selecciona la URL de imagen según la cancha.



### UI

Contiene Compose y ViewModel. UI observa estado y envía eventos al ViewModel.



### Navigation

AppNavigation implementa un NavHost con las rutas inicio y reservas



## Componentes Jetpack utilizados:



ViewModel: Administra estado de pantalla, coordina operaciones CRUD y separa lógica del compose.

StateFlow: Expone la lista de reservas como flujo observable y permite actualizar la interfaz cuando Room emite cambios.

Room: Implementa persistencia local mediante Reserva, ReservaDao, ReservaDatabase y ReservaRepository.

Navigation Compose: Administra la navegación entre la pantalla inicial y la pantalla de reservas.

Jetpack Compose: Construye la interfaz de la aplicación.

Lifecycle y viewModelScope: Permite ejecución de corrutinas asociadas al ciclo de vida del ViewModel.



## Librerías y herramientas



Kotlin Coroutines, Coil Compose, LeakCanary en debug, JUnit, Compose UI Test, Espresso Core, AndroidJUnitRunner, Logcat



## Pruebas unitarias



app/src/test/java/cl/duouc/reservasport/domain


### ReservaValidatorTest



Comprueba datos válidos, nombre vacío y cancha vacía.



### ImagenCanchaProviderTest



Comprueba imagen de tenis, imagen de fútbol, imagen predeterminada.



## Pruebas funcionales



app/src/androidTest/java/cl/duouc/reservasport/ReservaScreenTest.kt


Pruebas: inicio\_muestraBotonVerReservas, navegarAReservas\_muestraPantallaReservas, nuevaReserva\_abreFormulario, formularioVacio\_muestraMensajeValidacion, registrarReserva\_muestraReservaEnLista




## Ejecutar el proyecto

### Requisitos



* Android Studio
* JDK 11
* Android SDK 24 o superior
* Conexión a internet para Coil
* 

### Pasos

1. Clonar el repositorio.
2. Abrir el proyecto en Android Studio.
3. Sincronizar Gradle.
4. Seleccionar emulador o dispositivo.
5. Ejecutar el módulo `app`.



git clone https://github.com/lfavreau/APP\_MOVILESII\_S2.git
cd ReservaSport


## Ejecutar pruebas unitarias JUnit


./gradlew testDebugUnitTest


## Ejecutar pruebas funcionales



./gradlew connectedDebugAndroidTest


## Configuración


applicationId: cl.duouc.reservasport
minSdk: 24
targetSdk: 36
versionCode: 2
versionName: 1.1


La aplicación utiliza permiso de internet para cargar imágenes remotas.
