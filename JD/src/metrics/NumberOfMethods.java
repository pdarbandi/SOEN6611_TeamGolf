package metrics;

import java.util.HashMap;
import java.util.Map;

import ast.ClassObject;
import ast.SystemObject;

public class NumberOfMethods {

	private Map<String, Integer> classToNOMMap = new HashMap<String, Integer>();

	public NumberOfMethods(SystemObject system) {
		classToNOMMap.clear();

		for (ClassObject classObject : system.getClassObjects()) {
			int nom = classObject.getNumberOfMethods();
			classToNOMMap.put(classObject.getName(), nom);
		}
	}

	@Override
	public String toString() {
		return this.getNOM();
	}

	public String getNOM() {
		String result = "";
		for (String key : classToNOMMap.keySet()) {
			result += key + "\t" + classToNOMMap.get(key) + "\n";
		}
		return result + "\n";
	}

}