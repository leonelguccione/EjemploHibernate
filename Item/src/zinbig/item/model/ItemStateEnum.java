/**
 * Este paquete contiene las clases e interfaces que componen la capa 
 * del modelo.
 */
package zinbig.item.model;

/**
 * Las instancias de esta clase se utilizan para representar los estados en los
 * cuales se pueden encontrar los ítems.<br>
 * Este estado no es realmente un "estado" en el sentido del patrón de diseño
 * State, sino más bien una indicación de la situación del item.<br>
 * Los estados posibles para un ítem son: Creado (cuando se ha creado el ítem
 * pero todavía no se ha comenzado a trabajar en el mismo); Abierto (cuando se
 * lo ha asignado a una persona y está en un nodo del workflow); Cerrado (cuando
 * se ha finalizado el trabajo en el ítem, sea tanto porque se llegó a un nodo
 * final del workflow como cuando se ha cancelado el ítem); Bloqueado (cuando un
 * ítem se encuentra impedido de continuar, ya sea porque se ha dividido en
 * otros subitems que deben ser finalizados antes de que el ítem original pueda
 * continuar, o a raíz de una dependencia de otros ítems).
 * 
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public enum ItemStateEnum {
	/**
	 * Este es el primer estado por el que pasa un ítem. Cuando se crea un nuevo
	 * ítem, por defecto su estado inicial será CREATED, a menos que el usuario
	 * elija ya comenzar a trabajar sobre el mismo en cuyo caso el ítem será
	 * creado en el estado OPEN.
	 */
	CREATED(1),

	/**
	 * Cuando se desea comenzar a trabajar sobre un ítem recientemente creado,
	 * se debe asignar este estado al ítem y al mismo tiempo el nodo inicial del
	 * workflow del proyecto correspondiente. La estrategia de asignaciónde
	 * responsable del ítem será la encargada de definir qué usuario será el
	 * primer responsable.
	 */
	OPEN(2),

	/**
	 * Cuando un ítem se cierra o se cancela, debe pasar a este estado. Al mismo
	 * tiempo se lo debe pasar a un nodo final del workflow.
	 */
	CLOSED(3),

	/**
	 * Un ítem puede ser dividido en algún momento u depender de otros ítems. En
	 * estos casos el estado del ítem deberá ser BLOsCKED.
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
	 * @return un enumerativo según el valor recibido.
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
