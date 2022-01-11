package np.edu.scst.lab4;

public class ClassPerson {
    String name;
    int age;
    int salary = 30000;
    public ClassPerson(String a,int b, int c){
        this.name = a;
        this.age =  b;
        this.salary =  c;  
    }
    public ClassPerson(String a,int b){
        this.name = a;
        this.age =  b; 
    }
    public void print(){
        System.out.println("Name: "+name);
        System.out.println("Age: "+age);
        System.out.println("Salary: "+salary);
    }
    public static void main(String[] args) {
        ClassPerson c1 = new ClassPerson("Dipak",22,60000);
        ClassPerson c2 = new ClassPerson("Ram",25);
        c1.print();
        c2.print();
    }
}
