package unsafeDeserialization.factory;

public class ClassFactoryItem extends ClassFactory {

    @Override
    public FactoryInterface createFactoryInstance() {
        return new FactoryInterfaceImplementation();
    }

    public static FactoryInterface createNewInstance(FactoryInterface factoryInterface) {
        return new FactoryInterfaceImplementation(factoryInterface);
    }
}
