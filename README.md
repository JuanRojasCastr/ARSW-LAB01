# ARSW-LAB01
## Francisco Javier Rojas Y Juan Camilo Rojas

### Parte I - Introducción a Hilos en Java

1. De acuerdo con lo revisado en las lecturas, complete las clases CountThread, para que las mismas definan el ciclo 
de vida de un hilo que imprima por pantalla los números entre A y B.

![](https://cdn.discordapp.com/attachments/831560691252068372/1007049070801604749/unknown.png)

2. Complete el método __main__ de la clase CountMainThreads para que:
    1. Cree 3 hilos de tipo CountThread, asignándole al primero el intervalo [0..99], al segundo [99..199], y al tercero [200..299].
    2. Inicie los tres hilos con 'start()'.
    3. Ejecute y revise la salida por pantalla.
    4. Cambie el incio con 'start()' por 'run()'. Cómo cambia la salida?, por qué?.
       
       Start creea un nuevo hilo que ejecuta el metodo run() asociado a el mismo y run es el metodo o accion que ejecuta 
       el la instacia del Thread pero este no genera mas Thread o sub procesos por eso se ejecuta en el proceso principal.
   
    ![](https://cdn.discordapp.com/attachments/831560691252068372/1007049704481226852/unknown.png)
    ![](https://cdn.discordapp.com/attachments/831560691252068372/1007049777957064714/unknown.png)


### Parte II - Ejercicio Black List Search**


Para un software de vigilancia automática de seguridad informática se está desarrollando un componente encargado de validar las direcciones IP en varios miles de listas negras (de host maliciosos) conocidas, y reportar aquellas que existan en al menos cinco de dichas listas.

Dicho componente está diseñado de acuerdo con el siguiente diagrama, donde:

- HostBlackListsDataSourceFacade es una clase que ofrece una 'fachada' para realizar consultas en cualquiera de las N listas negras registradas (método 'isInBlacklistServer'), y que permite también hacer un reporte a una base de datos local de cuando una dirección IP se considera peligrosa. Esta clase NO ES MODIFICABLE, pero se sabe que es 'Thread-Safe'.

- HostBlackListsValidator es una clase que ofrece el método 'checkHost', el cual, a través de la clase 'HostBlackListDataSourceFacade', valida en cada una de las listas negras un host determinado. En dicho método está considerada la política de que al encontrarse un HOST en al menos cinco listas negras, el mismo será registrado como 'no confiable', o como 'confiable' en caso contrario. Adicionalmente, retornará la lista de los números de las 'listas negras' en donde se encontró registrado el HOST.

![](img/Model.png)

Al usarse el módulo, la evidencia de que se hizo el registro como 'confiable' o 'no confiable' se dá por lo mensajes de LOGs:

INFO: HOST 205.24.34.55 Reported as trustworthy

INFO: HOST 205.24.34.55 Reported as NOT trustworthy


Al programa de prueba provisto (Main), le toma sólo algunos segundos análizar y reportar la dirección provista (200.24.34.55), ya que la misma está registrada más de cinco veces en los primeros servidores, por lo que no requiere recorrerlos todos. Sin embargo, hacer la búsqueda en casos donde NO hay reportes, o donde los mismos están dispersos en las miles de listas negras, toma bastante tiempo.

Éste, como cualquier método de búsqueda, puede verse como un problema [vergonzosamente paralelo](https://en.wikipedia.org/wiki/Embarrassingly_parallel), ya que no existen dependencias entre una partición del problema y otra.

Para 'refactorizar' este código, y hacer que explote la capacidad multi-núcleo de la CPU del equipo, realice lo siguiente:

1. Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que haga la búsqueda de un segmento del conjunto de servidores disponibles. Agregue a dicha clase un método que permita 'preguntarle' a las instancias del mismo (los hilos) cuantas ocurrencias de servidores maliciosos ha encontrado o encontró.

![](https://cdn.discordapp.com/attachments/831560691252068372/1007131862168260618/unknown.png)
2. Agregue al método 'checkHost' un parámetro entero N, correspondiente al número de hilos entre los que se va a realizar la búsqueda (recuerde tener en cuenta si N es par o impar!). Modifique el código de este método para que divida el espacio de búsqueda entre las N partes indicadas, y paralelice la búsqueda a través de N hilos. Haga que dicha función espere hasta que los N hilos terminen de resolver su respectivo sub-problema, agregue las ocurrencias encontradas por cada hilo a la lista que retorna el método, y entonces calcule (sumando el total de ocurrencuas encontradas por cada hilo) si el número de ocurrencias es mayor o igual a _BLACK_LIST_ALARM_COUNT_. Si se da este caso, al final se DEBE reportar el host como confiable o no confiable, y mostrar el listado con los números de las listas negras respectivas. Para lograr este comportamiento de 'espera' revise el método [join](https://docs.oracle.com/javase/tutorial/essential/concurrency/join.html) del API de concurrencia de Java. Tenga también en cuenta:

    * Dentro del método checkHost Se debe mantener el LOG que informa, antes de retornar el resultado, el número de listas negras revisadas VS. el número de listas negras total (línea 60). Se debe garantizar que dicha información sea verídica bajo el nuevo esquema de procesamiento en paralelo planteado.

    * Se sabe que el HOST 202.24.34.55 está reportado en listas negras de una forma más dispersa, y que el host 212.24.24.55 NO está en ninguna lista negra.

![](https://cdn.discordapp.com/attachments/831560691252068372/1007128611024818226/unknown.png)
![](https://cdn.discordapp.com/attachments/831560691252068372/1007132128913391696/unknown.png)
**Parte II.I Para discutir la próxima clase (NO para implementar aún)**

La estrategia de paralelismo antes implementada es ineficiente en ciertos casos, pues la búsqueda se sigue realizando aún cuando los N hilos (en su conjunto) ya hayan encontrado el número mínimo de ocurrencias requeridas para reportar al servidor como malicioso. Cómo se podría modificar la implementación para minimizar el número de consultas en estos casos?, qué elemento nuevo traería esto al problema?

**Parte III - Evaluación de Desempeño**

A partir de lo anterior, implemente la siguiente secuencia de experimentos para realizar las validación de direcciones IP dispersas (por ejemplo 202.24.34.55), tomando los tiempos de ejecución de los mismos (asegúrese de hacerlos en la misma máquina):

1. Un solo hilo.
   CPU=0% MEM=37.554.288 B T=4 MIN 58 S
   ![](https://cdn.discordapp.com/attachments/831560691252068372/1007105575391330324/unknown.png)
2. Tantos hilos como núcleos de procesamiento (haga que el programa determine esto haciendo uso del [API Runtime](https://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html)).
   CPU=0% MEM=20.971.570 B T=2 MIN 57 S
   ![](https://cdn.discordapp.com/attachments/831560691252068372/1007107996247130172/unknown.png)
3. Tantos hilos como el doble de núcleos de procesamiento.
   CPU=0.1% MEM=18.874.368 B T= 1 MIN 44 S
   ![](https://cdn.discordapp.com/attachments/831560691252068372/1007108735883296860/unknown.png)
4. 50 hilos.
   CPU=12.7% MEM=32.505.865 T=1 MIN 40 S
   ![](https://cdn.discordapp.com/attachments/831560691252068372/1007113732750573568/unknown.png)
5. 100 hilos.
   CPU=28% MEM=77.594.624 T=1 MIN 43 S
   ![](https://cdn.discordapp.com/attachments/831560691252068372/1007116172782747749/unknown.png)

Al iniciar el programa ejecute el monitor jVisualVM, y a medida que corran las pruebas, revise y anote el consumo de CPU y de memoria en cada caso. ![](img/jvisualvm.png)

Con lo anterior, y con los tiempos de ejecución dados, haga una gráfica de tiempo de solución vs. número de hilos. Analice y plantee hipótesis con su compañero para las siguientes preguntas (puede tener en cuenta lo reportado por jVisualVM):

![](https://cdn.discordapp.com/attachments/831560691252068372/1007125687787868190/unknown.png)
segun vemos el comprtamiento de numero de threads en la busqueda de los servidores se comporta como una funcion racional o su grafica es similar.
claro que por la escala se distorciona un poco pero si graficamos puntos mas cercanos se parecria a una funcion 1/x y esto 
seguramente es debido a la division que realizamos para dividri los tramos