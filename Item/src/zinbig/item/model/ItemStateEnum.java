/**
 * Este paquete contiene las clases e interfaces que componen la capa 
 * del modelo.
 */
package zinbig.item.model;

/**
 * Las instancias de esta clase se utilizan para representar los estados en los
 * cuales se pueden encontrar los �tems.<br>
 * Este estado no es realmente un "estado" en el sentido del patr�n de dise�o
 * State, sino m�s bien una indicaci�n de la situaci�n del item.<br>
 * Los estados posibles para un �tem son: Creado (cuando se ha creado el �tem
 * pero todav�a no se ha comenzado a trabajar en el mismo); Abierto (cuando se
 * lo ha asignado a una persona y est� en un nodo del workflow); Cerrado (cuando
 * se ha finalizado el trabajo en el �tem, sea tanto porque se lleg� a un nodo
 * final del workflow como cuando se ha cancelado el �tem); Bloqueado (cuando un
 * �tem se encuentra impedido de continuar, ya sea porque se ha dividido en
 * otros subitems que deben ser finalizados antes de que el �tem original pueda
 * continuar, o a ra�z de una dependencia de otros �tems).
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public enum ItemStateEnum {
	/**
	 * Este es el primer estado por el que pasa un �tem. Cuando se crea un nuevo
	 * �tem, por defecto su estado inicial ser� CREATED, a menos que el usuario
	 * elija ya comenzar a trabajar sobre el mismo en cuyo caso el �tem ser�
	 * creado en el estado OPEN.
	 */
	CREATED(1),

	/**
	 * Cuando se desea comenzar a trabajar sobre un �tem recientemente creado,
	 * se debe asignar este estado al �tem y al mismo tiempo el nodo inicial del
	 * workflow del proyecto correspondiente. La estrategia de asignaci�nde
	 * responsable del �tem ser� la encargada de definir qu� usuario ser� el
	 * primer responsable.
	 */
	OPEN(2),

	/**
	 * Cuando un �tem se cierra o se cancela, debe pasar a este estado. Al mismo
	 * tiempo se lo debe pasar a un nodo final del workflow.
	 */
	CLOSED(3),

	/**
	 * Un �tem puede ser dividido en alg�n momento u depender de otros �tems. En
	 * estos casos el estado del �tem deber� ser BLOsCKED.
	 */
	BLOCKED(4);

	/**
	 * Es el valor de este enumerativo.
	 */
	protected Integer value;

	/**
	 * Constructor.
	 * 
	 * @param aValue
	 *            es el valor de este enumerativo.
	 */
	private ItemStateEnum(Integer aValue) {
		this.value = aValue;
	}

	/**
	 * Convierte el valor de este enumerativo a un entero.
	 * 
	 * @return el valor de este enumerativo.
	 */
	public Integer toInt() {
		return this.value;
	}

	/**
	 * Crea un enumerativo a partir de un valor entero.
	 * 
	 * @param value
	 *            es el valor entero.
	 * @return un enumerativo seg�n el valor recibido.
	 */
	public static ItemStateEnum fromInt(Integer value) {
		switch (value) {
		case 1:
			return CREATED;
		case 2:
			return OPEN;
		case 3:
			return CLOSED;
		case 4:
			return BLOCKED;
		default:
			return CREATED;
		}
	}

}
