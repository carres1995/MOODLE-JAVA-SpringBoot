### Propiedades de @Mapper
uses: Permite registrar otras clases de mapeo (o conversores personalizados) que MapStruct debe consultar si no sabe cómo convertir un tipo de dato específico.
unmappedTargetPolicy: Define qué hacer cuando hay campos en el destino que no tienen contraparte en el origen. Puedes usar ReportingPolicy.ERROR (falla la compilación) o IGNORE (lo pasa por alto).
collectionMappingStrategy: Controla cómo manejar colecciones (Listas, Sets). Puedes elegir entre ACCESSOR_ONLY (usar setters) o ADDER_PREFERRED (usar métodos addX).
builder: Permite configurar cómo MapStruct debe usar el patrón Builder si tus clases DTO o Entidades lo utilizan.
mappingInheritanceStrategy: Define si los métodos de mapeo deben heredar configuraciones de otros métodos (muy útil si tienes jerarquías de clases).

### @MappingTarget
Lo que sucede por debajo:
MapStruct toma los valores de UserDto.
Busca los campos correspondientes en UserEntity.
Utiliza los métodos setter de UserEntity para actualizar sus valores con los del UserDto.
No devuelve nada (o puede devolver la misma entidad), porque la modificación se hace directamente sobre el objeto entity original que pasaste por parámetro.

Conservar estados: Si usas @MappingTarget junto con nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE (que vimos antes),
logras un mecanismo de actualización muy robusto donde solo se guardan los cambios explícitos.