# Manual técnico de Archaios

**Asignatura:** Acceso a Datos  
**Curso:** 2.º de Desarrollo de Aplicaciones Multiplataforma  
**Entrega:** Segunda entrega — Acceso a ficheros  

**Autores:**

- Alberto Rodríguez Domínguez.
- Javier A. Goldar Picón.
- Constantin Laurentiu Mihalcia.
- Andrés Pacheco Castillo.

Archaios es una aplicación de consola que simula la excavación de tesoros por un equipo de arqueólogos. Permite gestionar sus integrantes, adquirir licencias regionales, consultar catálogos, realizar excavaciones, conservar partidas y enviar los hallazgos a un Museo compartido.

Este manual explica la organización por paquetes y la utilidad de las clases, interfaces, enumeraciones y tipos internos de Archaios. También presenta las dependencias externas utilizadas por la aplicación.

## Índice

1. [Paquete commons](#1-paquete-commons)
   - [Subpaquete commons.operations](#11-subpaquete-commonsoperations)
   - [Subpaquete commons.repositories](#12-subpaquete-commonsrepositories)
2. [Paquete excavation](#2-paquete-excavation)
3. [Paquete helpers](#3-paquete-helpers)
   - [Tipos internos de los componentes auxiliares](#31-tipos-internos-de-los-componentes-auxiliares)
4. [Paquete people](#4-paquete-people)
5. [Paquete regions](#5-paquete-regions)
6. [Paquete skills](#6-paquete-skills)
7. [Paquete treasures](#7-paquete-treasures)
8. [Dependencias externas](#8-dependencias-externas)


## 1. Paquete commons

Contiene los componentes que coordinan el funcionamiento general de la aplicación, mantienen el estado de la partida y presentan las consultas. Sus subpaquetes separan el contrato de las operaciones y el acceso a los catálogos compartidos.

- **`Archaios`** — Clase principal y coordinadora del sistema. Contiene `main()` y mantiene el UID del usuario, el nombre y el archivo de la partida, la lista de arqueólogos, las estadísticas y el saldo de puntos. Prepara los catálogos mediante `preInit()`, permite crear o cargar una partida mediante `init()` y organiza los menús. Coordina la contratación, el licenciamiento, las licencias regionales y las excavaciones; aplica sus resultados y solicita el guardado y el envío al Museo. `end()` cierra el lector compartido al terminar la aplicación.

- **`Atlas`** — Componente de consulta y presentación de las regiones y sus tesoros. Obtiene las regiones de `RegionRepository`, identifica las difíciles y muestra su expansión. Ordena una copia de cada catálogo por tipo y código, presenta las fichas y adapta el texto del tipo, la rareza y la división en partes. Sus consultas no requieren comprar la licencia necesaria para excavar en una región difícil.

- **`DatosPrueba`** — Componente auxiliar que prepara datos de ejemplo en memoria. Crea arqueólogos con distintos niveles y habilidades, estadísticas con visitas y piezas repetidas y un saldo inicial de 500 puntos. Se utiliza con el argumento `--pruebas`, que permite recorrer los menús sin crear un archivo de partida ni realizar envíos al Museo.

- **`Stats`** — Componente que conserva los contadores de visitas por código de región y de hallazgos por código completo de pieza. Presenta estadísticas por región o por tipo y diferencia las piezas distintas del total de hallazgos, incluidas las repeticiones. `getTreasureCounts()` y `getVisitCounts()` entregan copias de los mapas para el guardado; `restoreCounts()` sustituye sus contadores por los recibidos. La validación del contenido recuperado del fichero corresponde a `SaveManager`.

### 1.1 Subpaquete commons.operations

Define el contrato para identificar y ejecutar operaciones del sistema.

- **`IOperation`** — Interfaz que declara `getName()`, para obtener el nombre de una operación, y `operation()`, para ejecutar su lógica. Forma parte de la estructura proporcionada por la plantilla. El menú de esta versión ejecuta sus opciones directamente desde `Archaios`, sin clases que implementen esta interfaz.

### 1.2 Subpaquete commons.repositories

Centraliza las consultas a las regiones, las habilidades y los tesoros. Los repositorios activos de regiones y habilidades utilizan el almacenamiento genérico de Terra, explicado en el apartado de dependencias. El paquete también conserva el contrato y la base genérica local de la plantilla.

- **`IRegistrable`** — Interfaz local cuyo nombre completo es `commons.repositories.IRegistrable`. Exige el método `getCode()` para identificar un elemento registrable y es la restricción de tipo de la clase local `Repository`. Es un tipo distinto de `repositories.IRegistrable`, la interfaz de Terra que implementan `Region` y `Skill`.

- **`Repository`** — Clase abstracta genérica local de la plantilla. Mantiene un mapa de elementos indexados por código, declara `populate()` y ofrece registro y consultas mediante `register()`, `getElement()`, `getCodedElements()` y `getAllElements()`. Su parámetro genérico exige la interfaz local `IRegistrable`. `RegionRepository` y `SkillRepository` heredan de la base de Terra `repositories.Repositorio`, por lo que el flujo de esta versión no utiliza esta base local.

- **`RegionRepository`** — Repositorio compartido de regiones que hereda de `repositories.Repositorio<Region>`. Registra las instancias preparadas por `Archaios.preInit()` y permite recuperarlas por código. Entrega listas ordenadas por código y separa las regiones básicas de las difíciles comprobando si implementan `IDifficultRegion`. Su método `populate()` no carga catálogos: esa preparación se coordina desde `Archaios`.

- **`SkillRepository`** — Repositorio compartido de habilidades que hereda de `repositories.Repositorio<Skill>`. Construye una sola vez el catálogo y configura las dependencias y las incompatibilidades. Permite buscar por nombre, consultar por tipo y obtener las habilidades que puede aprender un arqueólogo mediante `getLearnable()`. Conserva las mismas instancias de las habilidades para que el aprendizaje y la recuperación de partidas mantengan sus relaciones.

- **`TreasureRepository`** — Componente de búsqueda de tesoros que consulta los catálogos de las regiones registradas. `getTreasure()` admite el código base de un tesoro o el código completo de una pieza, obtiene su región mediante `CodeHelper` y busca el código base en su catálogo. Devuelve el tesoro encontrado o `null` si no existe; no mantiene un segundo almacén independiente de tesoros.

## 2. Paquete excavation

Contiene la representación del área de excavación y el motor que desarrolla sus turnos. Separa las coordenadas, las reglas de los formatos de terreno, el estado de la rejilla y la gestión de los hallazgos.

- **`Coordinate`** — Clase que representa una posición mediante una fila y una columna que no cambian después de construirla. Sus índices internos comienzan en cero. Define `equals()` y `hashCode()` por esos dos valores, lo que permite usar las posiciones como claves de mapas y detectar coordenadas repetidas. `toString()` devuelve la representación interna de la posición.

- **`ETerrain`** — Enumeración de los 15 formatos de excavación: tres tamaños —pequeño, mediano y grande— combinados con cinco formas —fila, horizontal, columna, vertical y cuadrado—. Cada constante reúne columnas, filas, nivel mínimo, tesoros mínimos por rareza, permiso de aparición de raros y experiencia adicional. Los niveles de acceso por tamaño son 0, 2 y 4, y la experiencia adicional es 2, 5 y 10, respectivamente. Centraliza tanto las dimensiones como las reglas asociadas al tamaño; la disponibilidad final también depende de las formas que tenga desbloqueadas el arqueólogo.

- **`Excavation`** — Motor de una excavación concreta, asociado a una región, un arqueólogo y un formato `ETerrain`. Crea la rejilla, distribuye los terrenos físicos de las regiones difíciles, genera y coloca tesoros y aplica, por orden, las conversiones de terreno, la limpieza y el escaneo. Calcula las acciones con las bonificaciones del arqueólogo y gestiona cada intento usando el coste efectivo de la casilla. Finaliza cuando se agotan las acciones, se encuentran todos los tesoros o no queda ninguna casilla cuyo coste pueda pagarse. Proporciona los códigos y objetos encontrados, sus puntos y la experiencia obtenida; una sobrecarga de `excavate()` permite avisar a `Archaios` de cada hallazgo para registrarlo inmediatamente.

- **`Rejilla`** — Componente que almacena y dibuja el área de excavación. Separa tres datos: el estado visible de cada casilla, su terreno físico con su coste base y la rareza del tesoro oculto. Permite colocar tesoros, excavar, limpiar, marcar pistas, transformar terrenos y representar la rejilla durante la partida o completamente descubierta. Asigna el coste de la arena al crearla, de modo que no cambie al consultar una casilla. Conoce las rarezas, mientras que los objetos concretos y sus códigos los gestiona `Excavation`.

## 3. Paquete helpers

Agrupa componentes auxiliares para la entrada y validación de datos, la presentación, el acceso a ficheros y la comunicación con el Museo. Sus utilidades se reutilizan desde los menús, los repositorios y el motor de excavación.

- **`CodeHelper`** — Clase de utilidades para extraer la región, el tipo, el número de tesoro, el número de parte y el código base de un identificador. Comprueba si existe longitud suficiente para cada corte; si no la hay, devuelve la entrada recibida y conserva `null` cuando corresponde. Interpreta las posiciones de los fragmentos, pero la validación completa del formato se realiza en los componentes que leen los datos externos.

- **`CoordinateValidator`** — Componente que traduce entradas como `3B` o `10AA` a objetos `Coordinate`. `parseAll()` acepta coordenadas separadas por espacios o comas y rechaza la entrada completa si existe un formato incorrecto, una posición fuera de la rejilla, una casilla descubierta, una repetición o más coordenadas que acciones restantes. La comprobación del coste real y su descuento corresponden después a `Excavation`.

- **`InputValidator`** — Componente común de lectura por consola. Utiliza la consola nativa cuando está disponible y, en caso contrario, un lector compartido para evitar que los menús y la excavación compitan por la entrada. Valida opciones enteras dentro de un intervalo, lee textos y permite la lectura oculta de contraseñas cuando la consola lo admite. Detecta el fin de la entrada mediante su excepción interna `InputValidator.EndOfInputException`; `close()` libera el lector al finalizar.

- **`LogHelper`** — Componente de escritura de registros de texto en UTF-8. Crea el registro de una partida sin sobrescribir uno existente y añade el código completo de cada pieza en el momento del hallazgo. También añade a un registro global la descripción y los elementos de la traza de las excepciones. Cada entrada incorpora fecha y hora con el formato `yyyy-MM-dd HH:mm:ss`; los fallos de escritura se comunican al llamador.

- **`MenuHelper`** — Clase de utilidades de presentación. Imprime las cabeceras del menú principal, de las secciones y de las fichas con un formato común. También ofrece las preguntas reutilizables para repetir una consulta, volver o confirmar una acción, delegando la lectura validada en `InputValidator`.

- **`MuseoHelper`** — Componente de comunicación con el Museo mediante Terra. Recibe el UID y los códigos completos de la excavación, conserva sus repeticiones y escribe una cola local antes de intentar transmitirlos. Valida la configuración, agrupa los lotes por usuario y grupo, recupera los respaldos de Terra y reintenta los hallazgos conservados del mismo grupo junto con los nuevos. Retira de la cola los lotes que el Museo confirma como aceptados; ante errores informa y solicita su registro a `LogHelper`.

- **`SaveManager`** — Componente que lista, lee y escribe las partidas como JSON en UTF-8 con extensión `.save`. Convierte el estado del juego al formato de fichero y reconstruye sus arqueólogos, habilidades, licencias, especialización y estadísticas. Comprueba los campos, sus tipos y su coherencia con los catálogos y con la progresión del modelo antes de aceptar una carga o escritura. Escribe primero un temporal: las partidas nuevas no sobrescriben archivos existentes y las actualizaciones requieren sustitución atómica; si esta no está disponible, comunica el error y conserva el guardado anterior. Intercambia el estado con `Archaios` mediante el tipo interno `SaveManager.SavedGame`.

- **`TreasureLoader`** — Componente que carga los catálogos regionales JSON mediante Gson y construye los objetos `Treasure` en el orden del archivo. Lee los campos del tesoro, convierte los códigos de rareza y tipo y comprueba el formato del código base, su correspondencia con el tipo y el nombre del catálogo y la ausencia de códigos duplicados. Cierra los lectores al terminar. `Archaios` utiliza su resultado para construir cada región y detiene el arranque si un catálogo no puede cargarse o está vacío.

- **`UtilsHelper`** — Clase de utilidades generales. Incluye operaciones para poner en mayúscula la primera letra de una palabra o de las palabras de un texto, calcular porcentajes enteros y generar índices aleatorios sin repetición. Estas operaciones se utilizan en la presentación de información y en las selecciones aleatorias de las colecciones.

### 3.1 Tipos internos de los componentes auxiliares

Los siguientes tipos están declarados dentro de otros componentes y también forman parte del código del proyecto:

- **`InputValidator.EndOfInputException`** — Excepción interna que señala que la entrada por consola se ha cerrado antes de recibir una línea. `InputValidator.readLine()` la lanza y `Archaios.main()` la captura para terminar el flujo y cerrar el lector compartido. Evita que una ausencia de entrada se trate como una opción de menú incorrecta.

- **`SaveManager.SavedGame`** — Registro público de transferencia de estado. Agrupa el UID, el nombre de la partida, los arqueólogos, las estadísticas y los puntos para intercambiarlos entre `SaveManager` y `Archaios`. La lectura del gestor lo devuelve después de validar y reconstruir los datos; el mismo tipo se utiliza al solicitar una escritura.

- **`MuseoHelper.Configuration`** — Registro privado que conserva el grupo obtenido de la configuración validada del Museo. Se utiliza para asociar cada intento de envío y sus respaldos al grupo correspondiente. La dirección del servicio se valida al leer la configuración y la factoría de Terra la utiliza al crear el cliente.

- **`MuseoHelper.PendingSend`** — Registro privado que representa un lote de códigos completos de piezas y el grupo al que pertenece. Mantiene los códigos repetidos porque representan hallazgos distintos. Un lote creado sin configuración válida conserva el grupo sin asignar hasta el primer intento en el que la preparación de la configuración y del respaldo se completa correctamente.

- **`MuseoHelper.PendingData`** — Registro privado que agrupa el UID y la lista de lotes de envío conservados localmente. Es la estructura que `MuseoHelper` escribe en su fichero JSON de recuperación. Al leerlo, comprueba que corresponde al mismo UID antes de utilizar sus lotes.

## 4. Paquete people

Contiene el modelo del arqueólogo y las reglas de progresión, aprendizaje, permisos y bonificaciones que consume el motor de excavación.

- **`Archeologist`** — Clase que representa a un arqueólogo mediante su nombre, nivel, experiencia, habilidades, tamaños y formas de terreno disponibles, licencias regionales y especialización. `MAX_LEVEL` fija el nivel máximo en 5. `addXp()` consume la experiencia necesaria para cada subida mediante un bucle y devuelve cuántos niveles se ganaron; al alcanzar el máximo, la experiencia restante y la posterior se acumulan. `canLearn()` y `learnSkill()` comprueban duplicados, dependencias e incompatibilidades y añaden los formatos que desbloquea una habilidad. Los métodos de bonificación consultan las habilidades de forma polimórfica, suman sus aportaciones o combinan sus multiplicadores y aplican los extras regionales de especialización. El constructor de reconstrucción, `restoreSkills()` y `restoreRegionalData()` recuperan datos guardados sin simular nuevas subidas de nivel; las listas expuestas por sus getters no permiten modificaciones externas. `getLicensingPoints()` calcula diez puntos por habilidad aprendida.

## 5. Paquete regions

Contiene el modelo de las regiones, sus catálogos y la información de los terrenos difíciles. Todas las regiones concretas reciben una lista de tesoros ya cargada; la lectura de sus ficheros se coordina antes de registrarlas en el sistema.

- **`Region`** — Clase abstracta común a las regiones. Mantiene su catálogo de `Treasure`, exige un código identificativo y permite consultar todos los tesoros, filtrarlos por rareza y seleccionar uno aleatoriamente. Implementa la interfaz de Terra `repositories.IRegistrable` para incorporarse a `RegionRepository`. La clase base no realiza la lectura de los catálogos.

- **`IDifficultRegion`** — Interfaz que identifica las regiones difíciles. Declara `getExpansionName()` para obtener el nombre de la expansión y `getTerrainDistribution()` para obtener los porcentajes de cada terreno físico. `RegionRepository` la utiliza para clasificar las regiones, `Archaios` para ofrecer el acceso sujeto a licencia y `Excavation` para generar su distribución de casillas.

- **`Grecia`** — Región básica con código `GRC`. Recibe su catálogo cargado desde `resources/regions/GRC.json`. Se encuentra disponible para excavar sin licencia regional y utiliza el terreno básico de tierra.

- **`Roma`** — Región básica con código `ROM`. Recibe su catálogo cargado desde `resources/regions/ROM.json`. Comparte las reglas de acceso y el terreno básico de las regiones que no implementan `IDifficultRegion`.

- **`Iberia`** — Región básica con código `IBR`. Recibe su catálogo cargado desde `resources/regions/IBR.json`. Proporciona sus tesoros a las consultas y a las excavaciones sin exigir una licencia regional.

- **`Egipto`** — Región difícil con código `EGP`, perteneciente a «Imperios de las arenas». Recibe su catálogo desde `resources/regions/EGP.json` e implementa `IDifficultRegion`. Define una distribución del 10 % de agua, el 35 % de tierra y el 55 % de arena. Su acceso para excavar requiere la licencia de esta región.

- **`Petra`** — Región difícil con código `PTR`, de la expansión «Imperios de las arenas». Recibe su catálogo desde `resources/regions/PTR.json`. Define una distribución del 75 % de piedra y el 25 % de arena mediante `IDifficultRegion`, y exige su propia licencia regional para excavar.

- **`Sumeria`** — Región difícil con código `SUM`, de la expansión «Imperios de las arenas». Recibe su catálogo desde `resources/regions/SUM.json`. Define una distribución del 30 % de piedra, el 50 % de arena y el 20 % de tierra mediante `IDifficultRegion`. Su excavación también requiere la licencia correspondiente.

## 6. Paquete skills

Contiene la jerarquía de habilidades del arqueólogo. La clase base ofrece una interfaz común para sus efectos y las subclases sobrescriben los que les corresponden. Así, `Archeologist` puede combinar las bonificaciones recorriendo una lista de `Skill`, sin reproducir la implementación de cada subtipo.

- **`ESkillType`** — Enumeración que clasifica las habilidades en tesoro, excavación, terreno y especialista. Proporciona la representación textual de cada categoría, utilizada al crear las habilidades y al consultar el catálogo por tipo.

- **`Skill`** — Clase abstracta que reúne nombre, descripción, tipo, dependencias e incompatibilidades. Implementa la interfaz de Terra `repositories.IRegistrable`, usando el nombre de la habilidad como código de registro. Define valores neutros para los efectos que una subclase no aporta, como cero bonificaciones, ausencia de investigación o conservación del coste base. Las dependencias se añaden individualmente y las incompatibilidades se establecen en ambos sentidos; sus getters entregan listas no modificables.

- **`SkillExcavation`** — Subclase que representa las habilidades relacionadas con las acciones y la información inicial de la rejilla. Configura acciones adicionales, incrementos porcentuales de acciones, limpieza de casillas vacías, pistas de escaneo e investigación por rarezas. Sus métodos de creación facilitan construir las distintas familias de habilidades con los demás efectos a cero o desactivados. Los porcentajes de acciones los combina después `Archeologist` mediante factores multiplicativos.

- **`SkillTerrain`** — Subclase que aporta formatos adicionales de excavación. Mantiene una copia de las formas desbloqueadas y la expone como lista no modificable mediante `getTerrains()`. Representa las habilidades «Licencia A», que abre filas y columnas, y «Licencia B», que abre horizontales y verticales. Estas habilidades son independientes de las licencias regionales almacenadas en `Archeologist`.

- **`SkillTreasures`** — Subclase que modifica la generación de tesoros. Configura tesoros extra aleatorios o de una rareza concreta y aumentos de probabilidad para infrecuentes y raros. Ofrece constructores para las distintas combinaciones de esos efectos. `Archeologist` suma sus aportaciones y `Excavation` las aplica respetando los límites de tesoros y las reglas del tamaño de terreno.

- **`SkillTerrainSpecialist`** — Subclase que ofrece un coste de excavación positivo para un símbolo de terreno determinado y conserva el coste base para los demás. El catálogo la utiliza para fijar en una acción el coste de la arena. `Archeologist.getExcavationCost()` elige el menor coste entre el base y los ofrecidos por las habilidades; `Excavation` usa ese mismo resultado para comprobar si puede pagarse y para descontarlo.

- **`SkillDemolition`** — Subclase que indica cuántas casillas de un terreno concreto puede convertir a tierra. Valida que la cantidad configurada sea positiva y devuelve cero cuando se consulta otro símbolo. El catálogo incluye una habilidad que convierte hasta cinco piedras. `Excavation` elige las posiciones al azar y realiza las transformaciones antes de la limpieza y el escaneo, sin consumir acciones ni descubrir sus tesoros.

## 7. Paquete treasures

Contiene el modelo de los tesoros y las enumeraciones compartidas para sus rarezas y tipos. Estos datos conectan los catálogos regionales, las excavaciones, las estadísticas y los códigos enviados al Museo.

- **`Treasure`** — Clase que representa un tesoro del catálogo mediante su código base, nombre, descripción, rareza, tipo, número de partes y puntos otorgados por hallazgo. Sus getters y setters permiten consultar y modificar esos datos. `getRandomPartCode()` selecciona una parte y añade su número con cuatro dígitos al código base; devuelve `null` si el tesoro no tiene partes. Cada hallazgo se identifica con ese código completo, aunque corresponda a una pieza encontrada anteriormente.

- **`ETreasureRarity`** — Enumeración de las rarezas común, infrecuente y raro, con los códigos `C`, `I` y `R`. Proporciona el nombre y el código de cada rareza para los catálogos, la representación de la rejilla y los cálculos de generación. Incluye un método de búsqueda por código; la carga JSON de `TreasureLoader` realiza su propia conversión comparando el contenido de los códigos.

- **`ETreasureType`** — Enumeración de los ocho tipos de tesoro y sus códigos: moneda (`MON`), tesela (`TES`), cuenta (`CUE`), joyería (`JOY`), cerámica (`CER`), equipo (`EQP`), estela (`EST`) y fragmento (`FRA`). Proporciona sus nombres y códigos para las consultas, las estadísticas y la composición de identificadores. También incluye una búsqueda por código; `TreasureLoader` convierte los tipos del catálogo mediante su propia comparación de contenido.

## 8. Dependencias externas

La aplicación utiliza dos bibliotecas externas:

- **Gson:** permite leer y escribir los archivos JSON de catálogos, partidas y respaldos.
- **Terra:** proporciona los repositorios genéricos y la comunicación con el Museo.

La interfaz local `commons.repositories.IRegistrable` pertenece a la plantilla y es distinta de `repositories.IRegistrable`, proporcionada por Terra y utilizada por las regiones y las habilidades.


