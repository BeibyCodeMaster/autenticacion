package restaurante.example.demo.presentation.dto.user;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import restaurante.example.demo.persistence.enums.ActiveEnum;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// Objeto de Transferencia de Datos (DTO) que representa un administrador en el sistema.
// Contiene información sobre el usuario asociado al administrador y los detalles adicionales.
public class AdministratorDto {
    @Valid
    private UserDto user;  // Información del usuario asociado al administrador
    private Long administradorId;  // Identificador único del administrador
    private Date startDate;  // Fecha de inicio de la gestión del administrador
    private Date endDate;  // Fecha de finalización de la gestión del administrador
    private ActiveEnum state;  // Estado de la cuenta del administrador (activo o inactivo)
}
