package Source.sharedUtils;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class sharedData {

    public static currentUserInfo userInfo = null;


    public static class currentUserInfo {
        String surname;
        String id;
        String Email;
        String role;
        String other_names;

       public currentUserInfo(String id, String surname, String other_names, String email, String role){
            this.id = id;
            this.surname = surname;
            this.other_names = other_names;
            this.Email = email;
            this.role = role;
        }

        public String getSurname(){
           return this.surname;
        }
        public void setSurname(String s){
           this.surname = s;
        }

        public String getId(){
           return this.id;
        }
        public void setId(String i){
           this.id = i;
        }

        public String getEmail(){
           return this.Email;
        }
        public void setEmail(String e){
           this.Email = e;
        }

        public String getRole(){
           return this.role;
        }
        public void setRole(String r){
           this.role = r;
        }

        public String getOther_names(){
           return this.other_names;
        }
        public void setOther_names(String o){
           this.other_names = o;
        }

        public String getFullName(){
           return this.surname + " " + this.other_names;
        }
    }


    public static ObservableList<String> yearsList = FXCollections.observableArrayList(
            "2000",
            "2001", "2002", "2003", "2004", "2005", "2006",
            "2007", "2008", "2009", "2010", "2011", "2012",
            "2013", "2014", "2015", "2016", "2017", "2018",
            "2019", "2020", "2021", "2022", "2023", "2024",
            "2025", "2026", "2027", "2028", "2029", "2030",
            "2031", "2032", "2033", "2034", "2035", "2036",
            "2037", "2038", "2039", "2040", "2041", "2042",
            "2043", "2044", "2045", "2046", "2047", "2048",
            "2049", "2050"
    );

    public static ObservableList<String> academicYears = FXCollections.observableArrayList(
            "2018/2019", "2019/2020", "2020/2021", "2021/2022", "2022/2023", "2023/2024",
            "2024/2025", "2025/2026", "2026/2027", "2027/2028", "2028/2029", "2029/2030", "2030/2031",
            "2031/2032", "2032/2033", "2033/2034", "2034/2035"
    );
}
