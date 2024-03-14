package unsafeDeserialization.factory;

public class ClassFactoryItem extends ClassFactory {

    @Override
    public FactoryInterface createFactoryInstance() {
        return new FactoryInterfaceImplementation();
    }

    @Override
    public FactoryInterface createNewInstance(FactoryInterface factoryInterface) {
        return new FactoryInterfaceImplementation(factoryInterface);
    }
}
