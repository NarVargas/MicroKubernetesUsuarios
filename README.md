Este proyecto es una aplicación Java basada en microservicios que gestiona cursos y usuarios, desarrollada utilizando Spring Boot. 
Proporciona un backend seguro y eficiente para administrar información relacionada con cursos y usuarios. 

La aplicación utiliza tecnologías como:
  -Spring MVC para la creación de controladores que manejan las solicitudes HTTP.
  -Spring Data JPA para la interacción con bases de datos (PostgreSQL para cursos y MySQL para usuarios).
  -Spring Cloud OpenFeign para facilitar la comunicación entre microservicios de manera declarativa.
  
Funcionalidades Principales
La aplicación permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre las entidades de curso y usuario. Esto incluye detalles como:
  -Cursos: Gestión de información relacionada con los cursos, incluyendo nombre, descripción y usuarios asignados.
  -Usuarios: Administración de datos de usuarios, como nombre, apellidos, correo electrónico y otros detalles relevantes.
  

El proyecto está diseñado siguiendo las mejores prácticas de desarrollo de software y seguridad, con una estructura modular y bien organizada que facilita su mantenimiento y escalabilidad. Esto incluye:
  -Arquitectura de Microservicios: Cada componente (cursos y usuarios) se gestiona de manera independiente, lo que permite escalabilidad y una mayor resiliencia.
  -Configuración de Seguridad: Se establecen medidas de seguridad para proteger las comunicaciones y la información sensible.
  
Esta arquitectura no solo mejora la mantenibilidad y escalabilidad del sistema, sino que también permite adaptarse a las necesidades específicas del negocio.

