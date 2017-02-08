/**
 * Este paquete contiene todas las definiciones de las interfaces que los 
 * diferentes servicios deberán implementar.<br>
 * 
 */
package zinbig.item.services.bi;

import zinbig.item.util.dto.AbstractUserDTO;
import zinbig.item.util.dto.ItemDTO;
import zinbig.item.util.dto.UserDTO;

/**
 * Esta interface define el protocolo que debe ser implementado por los
 * servicios que envían email con notificaciones del sistema.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public interface EmailServiceBI {

	/**
	 * Envía un email al usuario para recordarle la clave generada
	 * automáticamente.
	 * 
	 * @param anUserDTO
	 *            es el usuario al que se le debe enviar la clave.
	 * @return true en caso de que se haya podido enviar el correo; false en
	 *         caso contrario.
	 */
	public boolean sendEmailToRememberPassword(UserDTO anUserDTO);

	/**
	 * Envía un email con información de un ítem a un usuario en particular.
	 * 
	 * @param anItemDTO
	 *            es el dto que representa al ítem que se está notificando.
	 * @param anUserDTO
	 *            es el dto que representa al usuario al cual se debe enviar el
	 *            email.
	 * @return true en caso de que se haya podido enviar el email; false en caso
	 *         contrario.
	 */
	public boolean sendEmailForItemToUser(ItemDTO anItemDTO,
			AbstractUserDTO anUserDTO);

}
