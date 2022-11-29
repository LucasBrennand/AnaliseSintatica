public class Main {
    public static void main(String[] args) {
        // TODO code application logic here
            Lexico lexico = new Lexico("src\\codigo.txt");
            //Sintatico1 sintatico = new Sintatico1(lexico);
            //sintatico.S();
            Sintatico2 sintatico2 = new Sintatico2(lexico);
            sintatico2.S();
        System.out.println("Compilação sucessiva");
    }

}