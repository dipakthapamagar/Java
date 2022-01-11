package np.edu.scst.lab6;

interface Parent1{
    public void method1();
    public void method2();
}
interface Parent2{
    public void method3();
    public void method4();
}
interface Child extends Parent1, Parent2{
    public void method5();
}
public class InterfaceInheritance implements Child{

    @Override
    public void method5() {
        System.out.println("Function5");
    }

    @Override
    public void method1() {
        System.out.println("Function1");
    }

    @Override
    public void method2() {
        System.out.println("Function2");
    }

    @Override
    public void method3() {
        System.out.println("Function3");
    }

    @Override
    public void method4() {
        System.out.println("Function4");
    }
    public static void main(String[] args) {
        InterfaceInheritance i = new InterfaceInheritance();
        i.method1();
        i.method2();
        i.method3();
        i.method4();
        i.method5();
    }
    
}
