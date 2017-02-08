/**
 * Este paquete contiene las clases necesarias para representar los objetos de 
 * dominio de una manera tal que puedan ser enviados a la capa de presentación <br>
 * Estas representaciones toman la forma de DTOs (Data Transfer Objects).
 */
package zinbig.item.util.dto;

import java.util.Comparator;

/**
 * Las instancias de esta clase se utilizan para ordenar los dtos que representan a los usuarios.<br>
 * El orden se establece comparando los apellidos y después los nombres.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 */
public class UserDTOComparator implements Comparator<UserDTO> {

  /**
   * Compara dos usuarios para ordenarlos según su apellido y nombre.
   * 
   * @return un entero que representa el orden que se les debe dar, de acuerdo a su apellido, nombre y
   *         username.
   */
  @Override
  public int compare(UserDTO anUser, UserDTO anotherUser) {
    int result = 0;

    try {
      result = anotherUser.getSurname() == null ? -1 : anUser.getSurname().compareTo(

      anotherUser.getSurname());
      if (result == 0) {
        result = anotherUser.getName() == null ? -1 : anUser.getName().compareTo(anotherUser.getName());

        if (result == 0) {
          result = anUser.getUsername().compareTo(anotherUser.getUsername());
        }
      }
    } catch (Exception e) {
    }
    return result;
  }

}
