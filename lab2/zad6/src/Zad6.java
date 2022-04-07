public class Zad6 {
    public static void main(String[] args) {
        Sheet sheet = new Sheet(5, 5);
        System.out.println();

        sheet.set("A1", "2");
        sheet.set("A2", "5");
        sheet.set("A3", "A1+A2");
        sheet.print();
        System.out.println();

        sheet.set("A1", "4");
        sheet.set("A4", "A1+A3");
        sheet.print();
        System.out.println();

        try {
            sheet.set("A1", "A3");
        } catch (Throwable throwable) {
            throwable.printStackTrace(System.out);
        }
        sheet.print();
        System.out.println();

        sheet.set("B1", "A1-A2+A3-A4+20");
        sheet.print();
        System.out.println();
    }
}
