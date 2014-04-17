package metrics;

import java.util.ArrayList;
import java.util.List;

import ast.ClassObject;
import ast.FieldObject;
import ast.MethodObject;
import ast.SystemObject;

public class MoodCouplingFactor {

	private SystemObject system;

	public MoodCouplingFactor(SystemObject system) {
		this.system = system;
	}

	public double getMoodCouplingFactor() {
		int totalNumberOfClasses;
		double numerator = 0;
		List<ClassObject> classObjects = new ArrayList<>(system.getClassObjects());
		totalNumberOfClasses = classObjects.size();

		for (int i = 0; i < totalNumberOfClasses; i++) {
			for (int j = 0; j < totalNumberOfClasses; j++) {
				if (!(classObjects.get(i).getName().equals(classObjects.get(j).getName()))) {
					numerator += calculateClient(classObjects.get(i), classObjects.get(j));
				}
			}
		}
		double denominator = totalNumberOfClasses * (totalNumberOfClasses - 1);
		return 100 * (numerator / denominator);
	}

	public int calculateClient(ClassObject clientClass, ClassObject supplier) {
		List<MethodObject> methodObjects = clientClass.getMethodList();
		List<FieldObject> accessedFields = new ArrayList<FieldObject>();
		for (int i = 0; i < methodObjects.size(); i++) {
			accessedFields.addAll(clientClass
					.getFieldsAccessedInsideMethod(methodObjects.get(i)));
		}
		List<String> classesOfAccessedFields = new ArrayList<String>();
		for (int i = 0; i < accessedFields.size(); i++) {
			classesOfAccessedFields.add(accessedFields.get(i).getClassName());
		}

		if (clientClass.isFriend(supplier.getName())
				|| clientClass.hasFieldType(supplier.getName())
				|| classesOfAccessedFields.contains(supplier.getName()))
			return 1;

		return 0;
	}

}