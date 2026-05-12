# Sistema de Gestión Clínica 🏥

Este proyecto es una aplicación desarrollada en **Java** diseñada para gestionar de manera eficiente los procesos operativos y administrativos de una clínica médica. El desarrollo se fundamenta en la aplicación rigurosa del paradigma de **Programación Orientada a Objetos (POO)**, asegurando un diseño modular, robusto y fácil de mantener.

## 🚀 Características Principales

*   **Gestión Integral de Pacientes:** Registro, modificación y consulta del historial de pacientes.
*   **Administración del Personal Médico:** Gestión de médicos agrupados por especialidad y horarios de disponibilidad.
*   **Sistema Ágil de Turnos:** Asignación, reprogramación y seguimiento de citas médicas.
*   **Persistencia de Datos en JSON:** Almacenamiento seguro y estructurado de toda la información operativa del sistema.

## 🏗️ Arquitectura y Principios POO

El sistema modela el entorno clínico real aplicando los pilares de la Orientación a Objetos:

*   **Abstracción:** Modelado de entidades clave (ej. *CitaMédica*, *HistoriaClínica*) ocultando la complejidad innecesaria.
*   **Encapsulamiento:** Protección de la integridad de los datos, garantizando que el acceso y modificación se realice mediante métodos controlados (`getters` y `setters`).
*   **Herencia:** Jerarquía de clases para evitar duplicidad de código (ej. clase base `Persona` extendida por `Paciente` y `Médico`).
*   **Polimorfismo:** Implementación de comportamientos diferenciados dependiendo de la clase específica (ej. diferentes tipos de atención o cálculo de honorarios).

## 🛠️ Tecnologías y Librerías

*   **Lenguaje Base:** Java
*   **Paradigma de Diseño:** Programación Orientada a Objetos (POO)
*   **Persistencia (Base de Datos Local):** Archivos `.json`.

## 🗄️ Modelo de Persistencia (Archivos JSON)

En lugar de requerir un servidor de base de datos externo, el sistema utiliza almacenamiento local estructurado en formato JSON, facilitando la portabilidad:
*   `pacientes.json`: Almacena el perfil, la información de contacto y el historial.
*   `medicos.json`: Contiene los profesionales y sus especialidades.
*   `turnos.json`: Registra la asignación de horarios entre pacientes y médicos.

## 🔧 Instalación y Ejecución

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/tu-usuario/nombre-del-repo.git](https://github.com/tu-usuario/nombre-del-repo.git)
