package zinbig.item.misc;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

import zinbig.item.util.IDGenerator;
import zinbig.item.util.dto.UserDTO;
import zinbig.item.util.security.SHA64EncryptionStrategy;

public class Tester2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ArrayList<String> array = new ArrayList<String>();
		//
		// array.add("[001] Ana Maria");
		// array.add("Ver todos");
		// array.add("[002] Arquitectura");
		//
		// Collator esCollator = Collator.getInstance(new Locale("es"));
		//
		// Collections.sort(array, esCollator);
		//
		// System.out.println("coleccion " + array);
		//
		// System.out.println(Pattern.matches("[a-zA-Z0-9\\s\\_\\-·ÌÛ˙¡…Õ”⁄Ò—]+",
		// "Prioridades-baÒo"));
		//
		// Map aMap = new HashMap();
		// aMap.put("uno", "1");
		// System.out.println("consultando al map " + aMap.get("dos"));
		//
		// File aFile = new File("c:/temp", "prueba1");
		// try {
		// aFile.createNewFile();
		// } catch (IOException e) {
		// 
		// e.printStackTrace();
		// }
		//
		// try {
		// String s = URLEncoder.encode("Prioridades b·sicas", "UTF-8");
		// System.out.println("string codificado " + s);
		// System.out.println("decodificado " + URLDecoder.decode(s, "UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// 
		// e.printStackTrace();
		// }
		//
		// System.out.println("valores " + ItemStateEnum.values());

		UserDTO dto = new UserDTO(IDGenerator.getId(), "username", "password",
				"es", "email", "name", "surname", true, 1, false, 0, false);
		dto.setOid(IDGenerator.getId());
		// Filter aFilter = new Filter("prueba");
		// aFilter.projectComponentClassNameStrategy =
		// "zinbig.item.model.filters.AnyPublicProjectFilterComponent";
		// aFilter.projectComponentClassNameStrategy =
		// "zinbig.item.model.filters.AnyProjectFilterComponent";

		// User anUser = new User();
		//
		// FilterStringCreationStrategy strategy = new
		// HibernateFilterStringCreationStrategy();
		//
		// strategy.createFilterString(aFilter);
		//
		// System.out.println("string " + aFilter.filterString);
		//
		// // , new Long(1),
		// // new Long(14),
		// // "zinbig.item.model.filters.CurrentUserAssigneeFilterComponent");
		// Vector vector = new Vector();
		// vector.add("1");
		// vector.add("2");
		System.out.println("clave "
				+ (new SHA64EncryptionStrategy()).encrypt("prod1"));

		Format formatter;
		Date aDate = new Date();

		Collection<Integer> prueba = new ArrayList<Integer>();
		prueba.add(1);
		prueba.add(2);
		prueba.add(3);
		System.out.println("prueba "
				+ prueba.toString().replace("[", "(").replace("]", ")"));

		System.out
				.println("resultado "
						+ Pattern
								.matches(
										"[a-zA-Z0-9\\s\\.\\&\\%\\$\\#\\*\\+\\:\\;\\,\\=\\/\\[\\]\\'\\\"\\-\\(\\)\\?\\!\\ø\\°\\_\\-·ÌÈÛ˙¡…Õ”⁄Ò—]+",
										"hola como \"flaco\" and·s.?-()"));

	}
}
