# Mendacium-poo
Documentación Técnica: Proyecto "Mendacium"

1. Contexto del Proyecto

Descripción y Objetivo

Este proyecto consiste en el desarrollo de un videojuego móvil de deducción social al estilo "Werewolf" o "Mafia", programado en Kotlin para Android. El objetivo principal del juego es enfrentar a una mayoría desinformada (el bando de los Buenos) contra una minoría informada (los Impostores).

El objetivo académico es consolidar los conceptos de la Programación Orientada a Objetos (POO) aplicándolos a un entorno de desarrollo móvil moderno, demostrando cómo estructurar reglas de negocio complejas mediante polimorfismo y encapsulación.


2. Reglas Básicas del Juego

Roles Activos: Al inicio de la partida, cada jugador recibe un rol secreto. En esta versión, se implementan cuatro roles fundamentales:

1. Aldeano: No tiene habilidades nocturnas. Su objetivo es deducir y votar de día.
2. Impostor: Despierta de noche para eliminar a un jugador.
3. Médico: Despierta de noche para proteger a un jugador (evitando que muera si es atacado por el Impostor).
5. Vidente: Despierta de noche para investigar a un jugador y descubrir su verdadero rol.


Fase de Noche: Los jugadores "duermen" en la interfaz. El sistema cede el turno secuencialmente a los roles con habilidades nocturnas (Impostor, Médico, Vidente) para que ejecuten sus acciones en secreto.

Fase de Día: El sistema calcula los resultados de la noche (evaluando ataques y protecciones). Se revela quién fue eliminado (si no fue protegido). Todos los jugadores vivos discuten y votan para "linchar" a un sospechoso.

Condición de Victoria: El bando bueno gana si elimina a todos los impostores. Los impostores ganan si su número iguala o supera al de los jugadores restantes.



3. Lógica del Juego (Flujo de Partida)

Lobby: Se registran los nombres. El creador inicia la partida.

Repartición: La clase Partida mezcla internamente los roles requeridos (ej. 1 Impostor, 1 Médico, 1 Vidente, N Aldeanos) y los asigna.

Fase de Noche:

El ViewModel establece el estado en NOCHE.
Se itera sobre los jugadores con habilidades activas.
Se presenta una pantalla a cada rol especial para elegir su objetivo, invocando jugador.rol.realizarAccion(objetivo).
Resolución Nocturna:
El motor del juego evalúa el choque de acciones. Si el objetivo del Impostor tiene isProtected == true (por obra del Médico), la muerte se cancela.


Fase de Día:

Se limpian los modificadores temporales (isProtected = false).
Se anuncia el resultado de la noche. Los jugadores debaten.

Votación y Linchamiento:

Los jugadores vivos emiten un voto.
Se cuenta el mayor número de votos y el jugador seleccionado es eliminado.
Se verifica la condición de victoria. Si nadie ha ganado, se vuelve al paso 3.


Anexos

Vidente:
https://www.figma.com/design/SaDVhGG7S6G64fDYHYjRJL/Vidente-view?node-id=0-1&t=KvdsS3NL35hPfFT9-1 

Impostor:
https://www.figma.com/design/PAzgK0WK0ahNdCJCsk6rvD/Importor-view?t=KvdsS3NL35hPfFT9-1

Médico:
https://www.figma.com/design/KhFsSBRLYBdgNO6f4kQFJi/Medico-view?t=KvdsS3NL35hPfFT9-1

Aldeano:
https://www.figma.com/design/qIJbyYNRIz5utOg0PycCdl/Aldeano-view?node-id=0-1&t=A7IMUh5Zsk6OmMee-1

Lobo:
https://www.figma.com/design/ehL5MKlCbFbOs7UegVMt8K/Untitled?node-id=1-3&t=1kpP4jionRFB1kdd-1
