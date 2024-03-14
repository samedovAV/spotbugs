package unsafeDeserialization.factory;

public abstract class ClassFactory {

    public FactoryInterface create() {
        FactoryInterface instance = createFactoryInstance();
        instance.build();
        return instance;
    }

    public FactoryInterface newInstance(FactoryInterface factoryInterface) {
        FactoryInterface instance = createNewInstance(factoryInterface);
        instance.build();
        return instance;
    }

    public abstract FactoryInterface createFactoryInstance();

    public abstract FactoryInterface createNewInstance(FactoryInterface factoryInterface);
}
