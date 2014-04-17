package metrics;

import java.util.HashSet;
import java.util.Set;

import ast.Access;
import ast.ClassObject;
import ast.MethodObject;
import ast.SystemObject;

public class MoodPolymorphismFactor {

	private SystemObject system;
	public MoodPolymorphismFactor(SystemObject system) {
		this.system = system;
	}
	public float getMoodPolymorphismFactor() {
		int numerator = 0, denominator = 0;
		Set<ClassObject> allSystemClasses = system.getClassObjects();
		for (ClassObject currentClass : allSystemClasses) {
			int numberOfNewMethods = 0, numberOfChildren = 0;
			Set<MethodObject> overriddenMethodsSet = new HashSet<>();
			Set<ClassObject> superClasses = getAllAncestors(system,
					currentClass);
			for (ClassObject superClass : superClasses) {
				if (superClass == null)
					continue;
				for (MethodObject methodInChildClass : currentClass
						.getMethodList()) {
					if (!methodInChildClass.getAccess().equals(Access.PRIVATE)) {
						for (MethodObject methodInSuperClass : superClass
								.getMethodList()) {
							if (methodInSuperClass.getName().equals(
									methodInChildClass.getName())
									&& methodInSuperClass
											.getParameterTypeList()
											.equals(methodInChildClass
													.getParameterTypeList()))
								overriddenMethodsSet.add(methodInChildClass);
						}
					}
				}
			}
			for (MethodObject methodInChildClass : currentClass.getMethodList()) {
				if (!overriddenMethodsSet.contains(methodInChildClass)) {
					numberOfNewMethods++;
				}
			}
			for (ClassObject classObject : allSystemClasses) {

				if (classObject == currentClass)
					continue;

				Set<ClassObject> ancestors = getAllAncestors(system, classObject);
				if (ancestors.contains(currentClass))
					numberOfChildren++;
			}
			numerator += overriddenMethodsSet.size();
			denominator += numberOfNewMethods * numberOfChildren;
		}
		if (denominator != 0)
			return (float) (numerator / denominator) * 100;
		else
			throw new IllegalArgumentException("Division by zero");
	}

	private Set<ClassObject> getAllAncestors(SystemObject system,
			ClassObject childClass) {

		Set<ClassObject> toReturn = new HashSet<>();

		ClassObject superClass;
		do {
			superClass = null;

			if (childClass.getSuperclass() != null) {

				superClass = system.getClassObject(childClass.getSuperclass()
						.getClassType());

				toReturn.add(superClass);

				childClass = superClass;
			}
		} while (superClass != null);

		return toReturn;

	}

}
