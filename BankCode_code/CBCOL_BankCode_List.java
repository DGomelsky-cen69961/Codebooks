package cz.csas.colmanbatch.csv2xml;

public class CBCOL_BankCode_List implements Comparable<CBCOL_BankCode_List> {
    private final String id;
    private final String value;
    private final int entryOrder;
    private final String cluid;

    CBCOL_BankCode_List(String var1, String var2, String var3, int var4) {
        this.id = var1;
        this.value = var2;
        this.cluid = var3;
        this.entryOrder = var4;
    }

    String getvalue() {
        return this.value;
    }

    String getId() {
        return this.id;
    }

    int getEntryOrder() {
        return this.entryOrder;
    }

    String getCluid() {
        return this.cluid;
    }



    public int compareTo(CBCOL_BankCode_List var1) {
        int var2 = var1.getEntryOrder();
        return this.entryOrder - var2;
    }

    public String toString() {
        return " " + this.id + " " + this.entryOrder + " " + this.value;
    }
}
