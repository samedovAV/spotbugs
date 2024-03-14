package unsafeDeserialization.factory;

public class FactoryInterfaceImplementation implements FactoryInterface {

    public FactoryInterfaceImplementation() {
    }

    public FactoryInterfaceImplementation(FactoryInterface factoryInterface) {

    }

    @Override
    public void build() {
        System.out.println("Build");
    }
}
