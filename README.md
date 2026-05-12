Sistema de Gestión Clínica 🏥

Este proyecto es una aplicación desarrollada en Java diseñada para gestionar de manera eficiente los procesos operativos y administrativos de una clínica médica. El desarrollo se fundamenta en la aplicación rigurosa del paradigma de Programación Orientada a Objetos (POO), asegurando un diseño modular, robusto y fácil de mantener.

🚀 Características Principales
Gestión Integral de Pacientes: Registro, modificación y consulta del historial de pacientes.

Administración del Personal Médico: Gestión de médicos agrupados por especialidad y horarios de disponibilidad.

Sistema Ágil de Turnos: Asignación, reprogramación y seguimiento de citas médicas.

Persistencia de Datos en JSON: Almacenamiento seguro y estructurado de toda la información operativa del sistema.

🏗️ Arquitectura y Principios POO
El sistema modela el entorno clínico real aplicando los pilares de la Orientación a Objetos:

Abstracción: Modelado de entidades clave (ej. CitaMédica, HistoriaClínica) ocultando la complejidad innecesaria.

Encapsulamiento: Protección de la integridad de los datos, garantizando que el acceso y modificación (como la actualización de un turno) se realice mediante métodos controlados.

Herencia: Jerarquía de clases para evitar duplicidad de código (ej. clase base Persona extendida por Paciente y Médico).

Polimorfismo: Implementación de comportamientos diferenciados (ej. cálculo de honorarios dependiendo de la especialidad del médico o el tipo de consulta).

🛠️ Tecnologías y Librerías
Lenguaje Base: Java (JDK 17+)

Paradigma de Diseño: Orientado a Objetos (POO)

Persistencia (Base de Datos Local): Archivos .json.

🗄️ Modelo de Persistencia (Archivos JSON)
En lugar de requerir un servidor de base de datos externo, el sistema utiliza almacenamiento local estructurado en formato JSON, facilitando la portabilidad:

pacientes.json: Almacena el perfil y la información de contacto.

medicos.json: Contiene los profesionales y sus especialidades.

turnos.json: Registra la asignación de horarios entre pacientes y médicos.

🔧 Instalación y Ejecución
Clonar el repositorio:

Bash
git clone https://github.com/tu-usuario/nombre-del-repo.git
Configurar Librerías Externas: Si utilizaste librerías externas para leer/escribir JSON (como Gson o Jackson), asegúrate de añadirlas al .classpath de tu IDE o incluirlas en tu pom.xml si usas Maven.

Abrir el proyecto: Importar el proyecto en IntelliJ IDEA, Eclipse o NetBeans.

Ejecución: Ejecutar la clase principal Main.java para iniciar el sistema.
