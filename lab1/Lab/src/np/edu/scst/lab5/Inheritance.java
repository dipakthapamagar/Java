package np.edu.scst.lab5;
class A{
    void run(){
        System.out.println("Parent");
    }
}
class B extends A{
    void print(){
        System.out.println("Child of parent");
    }
}
class Inheritance extends B{
    void Say(){
        System.out.println("I am grandchild.");
    }
    public static void main(String[] args) {
        Inheritance a = new Inheritance();
        a.run();
        a.print();
        a.Say();
    }
}
