package cz.csas.colmanbatch.csv2xml;

public class CBCOL_CadastreProtectionMethod_List implements Comparable<CBCOL_CadastreProtectionMethod_List> {
    private final String id;
    private final String value;
    private final int entryOrder;
    private final String valid;

    CBCOL_CadastreProtectionMethod_List(String var1, String var2, int var3, String var4) {
        this.id = var1;
        this.value = var2;
        this.entryOrder = var3;
        this.valid = var4;
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

    String getValid() {
        return this.valid;
    }



    public int compareTo(CBCOL_CadastreProtectionMethod_List var1) {
        int var2 = var1.getEntryOrder();
        return this.entryOrder - var2;
    }

    public String toString() {
        return " " + this.id + " " + this.entryOrder + " " + this.value;
    }
}
