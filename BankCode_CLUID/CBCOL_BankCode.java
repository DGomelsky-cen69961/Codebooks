package cz.csas.colmanbatch.csv2xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.stereotype.Component;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class CBCOL_BankCode extends Csv2Xml {
    private String id_pom;
    private String bankName_pom;
    private int entryOrder_pom;
    private String cluid_pom;
    ArrayList<CBCOL_BankCode_List> seznam = new ArrayList();
    private final String inputFile = "CB_BankCode.csv";
    private final String outputFile1 = "CodeTableMapping_CBCOL_BankCode_CDM.xml";
    private final String outputFile2 = "UniversalLanguage_CBCOL_BankCode.xml";
    private final String outputFile3 = "wdp_CBCOL_BankCode.xml";

    CBCOL_BankCode() {
    }

    public void load() {
        this.loadFile2Array(this.inputDir);
        this.array2XML(this.outputDir);
    }

    public void loadFile2Array(String inputDir) {
        try {
            new FileReader(inputDir + "CB_OrganizationUnit.csv");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputDir + "CB_BankCode.csv"), "ISO-8859-2"));
            in.readLine();

            String radka;
            while((radka = in.readLine()) != null) {
                int fromIndex = 0;
                int index_1 = radka.indexOf("\"", fromIndex);
                fromIndex = index_1 + 1;
                int index_2 = radka.indexOf("\";\"", fromIndex);
                this.id_pom = radka.substring(index_1 + 1, index_2);
                fromIndex = index_2 + 1;
                int index_3 = radka.indexOf("\";\"", fromIndex);
                this.bankName_pom = radka.substring(index_2 + 3, index_3);
                fromIndex = index_3 + 1;
                int index_4 = radka.indexOf("\";\"", fromIndex);
                this.entryOrder_pom = Integer.parseInt(radka.substring(index_3 + 3, index_4));
                int index_5 = radka.lastIndexOf("\"");
                this.cluid_pom = radka.substring(index_4 + 3, index_5);
                this.seznam.add(new CBCOL_BankCode_List(this.id_pom, this.bankName_pom, this.entryOrder_pom, this.cluid_pom));
            }
        } catch (Exception var10) {
            if (var10 instanceof RuntimeException) {
                throw (RuntimeException)var10;
            }

            throw new RuntimeException(var10);
        }

        System.out.println("Loaded ArrayList CB_BankCode has: " + this.seznam.size() + " rows");
        Collections.sort(this.seznam);
    }

    public void array2XML(String outputDir) {
        try {
            DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
            Document doc1 = dBuilder1.newDocument();
            Element rootElement1 = doc1.createElement("set");
            doc1.appendChild(rootElement1);
            Iterator var6 = this.seznam.iterator();

            Element rootElement2;
            Element universalLanguage2;
            Element state2;
            Element cluid;
            while(var6.hasNext()) {
                CBCOL_BankCode_List org1 = (CBCOL_BankCode_List)var6.next();
                String idSeznam1 = org1.getId();
                int getEntryOrderSeznam1 = org1.getEntryOrder();
                String bankNameSeznam1 = org1.getBankName();
                String cluidSeznam1 = org1.getCluid();
                Element codeTableMap1 = doc1.createElement("cz.csas.colman.model.codetablemap.CodeTableMap");
                rootElement1.appendChild(codeTableMap1);
                Element searchMode = doc1.createElement("searchMode");
                searchMode.appendChild(doc1.createTextNode("StartsWith"));
                codeTableMap1.appendChild(searchMode);
                rootElement2 = doc1.createElement("state");
                rootElement2.appendChild(doc1.createCDATASection("CREATED"));
                codeTableMap1.appendChild(rootElement2);
                Element idecko1 = doc1.createElement("id");
                codeTableMap1.appendChild(idecko1);
                idecko1.appendChild(doc1.createCDATASection("cscollateral/CBCOL_BankCode/" + idSeznam1 + "/mapping/CDM/" + idSeznam1));
                Element codeTableCode1 = doc1.createElement("codeTableCode");
                codeTableMap1.appendChild(codeTableCode1);
                codeTableCode1.appendChild(doc1.createCDATASection("CBCOL_BankCode"));
                Element codeTableItemName1 = doc1.createElement("codeTableItemName");
                codeTableMap1.appendChild(codeTableItemName1);
                codeTableItemName1.appendChild(doc1.createCDATASection(idSeznam1));
                Element isPrimaryIn1 = doc1.createElement("isPrimaryIn");
                codeTableMap1.appendChild(isPrimaryIn1);
                isPrimaryIn1.appendChild(doc1.createTextNode("true"));
                Element isPrimaryOut1 = doc1.createElement("isPrimaryOut");
                codeTableMap1.appendChild(isPrimaryOut1);
                isPrimaryOut1.appendChild(doc1.createTextNode("true"));
                universalLanguage2 = doc1.createElement("mappedValue");
                codeTableMap1.appendChild(universalLanguage2);
                universalLanguage2.appendChild(doc1.createCDATASection(idSeznam1));
                state2 = doc1.createElement("system");
                codeTableMap1.appendChild(state2);
                state2.appendChild(doc1.createCDATASection("CDM"));
                cluid = doc1.createElement("CLUID");
                codeTableMap1.appendChild(cluid);
                cluid.appendChild(doc1.createCDATASection("cscollateral/CBCOL_BankCode/" + cluidSeznam1 + "/mapping/CDM/" + cluidSeznam1));
            }

            TransformerFactory transformerFactory1 = TransformerFactory.newInstance();
            Transformer transformer1 = transformerFactory1.newTransformer();
            DOMSource source1 = new DOMSource(doc1);
            transformer1.setOutputProperty("encoding", "UTF-8");
            transformer1.setOutputProperty("indent", "yes");
            transformer1.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result1 = new StreamResult(new File(outputDir + "CodeTableMapping_CBCOL_BankCode_CDM.xml"));
            transformer1.transform(source1, result1);
            DocumentBuilderFactory dbFactory2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder2 = dbFactory2.newDocumentBuilder();
            Document doc2 = dBuilder2.newDocument();
            rootElement2 = doc2.createElement("set");
            doc2.appendChild(rootElement2);
            Iterator var59 = this.seznam.iterator();

            Element rootElement3;
            Element searchMode;
            Element state3;
            Element iD3;
            Element referenceName3;
            Element displayNameUniv3;
            Element fieldType3;
            Element module3;
            Element state31;
            while(var59.hasNext()) {
                CBCOL_BankCode_List org2 = (CBCOL_BankCode_List)var59.next();
                String idSeznam2 = org2.getId();
                int getEntryOrderSeznam2 = org2.getEntryOrder();
                String bankNameSeznam2 = org2.getBankName();
                String cluidSeznam2 = org2.getCluid();
                universalLanguage2 = doc2.createElement("hu.appello.webdp.model.UniversalLanguage");
                rootElement2.appendChild(universalLanguage2);
                state2 = doc2.createElement("state");
                state2.appendChild(doc2.createTextNode("CREATED"));
                universalLanguage2.appendChild(state2);
                rootElement3 = doc2.createElement("id");
                universalLanguage2.appendChild(rootElement3);
                rootElement3.appendChild(doc2.createCDATASection("cscollateral/CBCOL_BankCode-" + idSeznam2));
                searchMode = doc2.createElement("hu");
                universalLanguage2.appendChild(searchMode);
                searchMode.appendChild(doc2.createCDATASection(bankNameSeznam2));
                state3 = doc2.createElement("en");
                universalLanguage2.appendChild(state3);
                state3.appendChild(doc2.createCDATASection(bankNameSeznam2));
                iD3 = doc2.createElement("cz");
                universalLanguage2.appendChild(iD3);
                iD3.appendChild(doc2.createCDATASection(bankNameSeznam2));
                referenceName3 = doc2.createElement("module");
                universalLanguage2.appendChild(referenceName3);
                displayNameUniv3 = doc2.createElement("state");
                displayNameUniv3.appendChild(doc2.createCDATASection("CREATED"));
                referenceName3.appendChild(displayNameUniv3);
                fieldType3 = doc2.createElement("id");
                referenceName3.appendChild(fieldType3);
                fieldType3.appendChild(doc2.createCDATASection("cscollateral"));
                module3 = doc2.createElement("referenceName");
                referenceName3.appendChild(module3);
                module3.appendChild(doc2.createCDATASection("cscollateral"));
                state31 = doc2.createElement("displayNameUniv");
                referenceName3.appendChild(state31);
                state31.appendChild(doc2.createCDATASection("cscollateral/modultitle"));
            }

            TransformerFactory transformerFactory2 = TransformerFactory.newInstance();
            Transformer transformer2 = transformerFactory2.newTransformer();
            DOMSource source2 = new DOMSource(doc2);
            transformer2.setOutputProperty("encoding", "UTF-8");
            transformer2.setOutputProperty("indent", "yes");
            transformer2.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result2 = new StreamResult(new File(outputDir + "UniversalLanguage_CBCOL_BankCode.xml"));
            transformer2.transform(source2, result2);
            DocumentBuilderFactory dbFactory3 = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder3 = dbFactory3.newDocumentBuilder();
            Document doc3 = dBuilder3.newDocument();
            rootElement3 = doc3.createElement("hu.appello.webdp.model.CodeBase");
            doc3.appendChild(rootElement3);
            searchMode = doc3.createElement("searchMode");
            rootElement3.appendChild(searchMode);
            searchMode.appendChild(doc3.createTextNode("StartsWith"));
            state3 = doc3.createElement("state");
            rootElement3.appendChild(state3);
            state3.appendChild(doc3.createCDATASection("CREATED"));
            iD3 = doc3.createElement("id");
            rootElement3.appendChild(iD3);
            iD3.appendChild(doc3.createCDATASection("cscollateral/CBCOL_BankCode"));
            referenceName3 = doc3.createElement("referenceName");
            rootElement3.appendChild(referenceName3);
            referenceName3.appendChild(doc3.createCDATASection("CBCOL_BankCode"));
            displayNameUniv3 = doc3.createElement("displayNameUniv");
            rootElement3.appendChild(displayNameUniv3);
            displayNameUniv3.appendChild(doc3.createCDATASection("cscollateral/CBCOL_BankCode"));
            fieldType3 = doc3.createElement("fieldType");
            rootElement3.appendChild(fieldType3);
            fieldType3.appendChild(doc3.createCDATASection("StringField"));
            module3 = doc3.createElement("module");
            rootElement3.appendChild(module3);
            state31 = doc3.createElement("state");
            state31.appendChild(doc3.createCDATASection("CREATED"));
            module3.appendChild(state31);
            Element id31 = doc3.createElement("id");
            module3.appendChild(id31);
            id31.appendChild(doc3.createCDATASection("cscollateral"));
            Element referenceName31 = doc3.createElement("referenceName");
            module3.appendChild(referenceName31);
            referenceName31.appendChild(doc3.createCDATASection("cscollateral"));
            Element displayNameUniv31 = doc3.createElement("displayNameUniv");
            module3.appendChild(displayNameUniv31);
            displayNameUniv31.appendChild(doc3.createCDATASection("cscollateral/modultitle"));
            Element codeElements3 = doc3.createElement("codeElements");
            rootElement3.appendChild(codeElements3);
            Iterator var34 = this.seznam.iterator();

            while(var34.hasNext()) {
                CBCOL_BankCode_List org3 = (CBCOL_BankCode_List)var34.next();
                Element codeElement3 = doc3.createElement("hu.appello.webdp.model.CodeElement");
                codeElements3.appendChild(codeElement3);
                String id32 = org3.getId();
                int getEntryOrder32 = org3.getEntryOrder();
                String value32 = org3.getBankName();
                String cluidSeznam3 = org3.getCluid();
                Element element3 = doc3.createElement("state");
                codeElement3.appendChild(element3);
                element3.appendChild(doc3.createCDATASection("CREATED"));
                Element id3 = doc3.createElement("id");
                codeElement3.appendChild(id3);
                String pom = org3.getId();
                id3.appendChild(doc3.createCDATASection("cscollateral/CBCOL_BankCode/" + pom));
                Element referenceName32 = doc3.createElement("referenceName");
                codeElement3.appendChild(referenceName32);
                referenceName32.appendChild(doc3.createCDATASection(pom));
                Element sortOrder3 = doc3.createElement("sortOrder");
                codeElement3.appendChild(sortOrder3);
                sortOrder3.appendChild(doc3.createCDATASection(String.valueOf(org3.getEntryOrder())));
                Element active3 = doc3.createElement("active");
                codeElement3.appendChild(active3);
                active3.appendChild(doc3.createTextNode("true"));
                Element displayNameUniv32 = doc3.createElement("displayNameUniv");
                codeElement3.appendChild(displayNameUniv32);
                displayNameUniv32.appendChild(doc3.createCDATASection("cscollateral/CBCOL_BankCode-" + pom));
                Element usable3 = doc3.createElement("usable");
                codeElement3.appendChild(usable3);
                usable3.appendChild(doc3.createTextNode("true"));
                Element codeBase3 = doc3.createElement("codeBase");
                Attr attrType33 = doc3.createAttribute("reference");
                attrType33.setValue("../../..");
                codeBase3.setAttributeNode(attrType33);
                codeElement3.appendChild(codeBase3);
                Element description3 = doc3.createElement("description");
                codeElement3.appendChild(description3);
                description3.appendChild(doc3.createCDATASection(" "));
                Element displayCluid = doc3.createElement("displayCluid");
                codeElement3.appendChild(displayCluid);
                displayCluid.appendChild(doc3.createCDATASection("cscollateral/CBCOL_BankCode-" + cluidSeznam3));
            }

            TransformerFactory transformerFactory3 = TransformerFactory.newInstance();
            Transformer transformer3 = transformerFactory3.newTransformer();
            DOMSource source3 = new DOMSource(doc3);
            transformer3.setOutputProperty("encoding", "UTF-8");
            transformer3.setOutputProperty("indent", "yes");
            transformer3.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result3 = new StreamResult(new File(outputDir + "wdp_CBCOL_BankCode.xml"));
            transformer3.transform(source3, result3);
        } catch (Exception var51) {
            var51.printStackTrace();
            if (var51 instanceof RuntimeException) {
                throw (RuntimeException)var51;
            } else {
                throw new RuntimeException(var51);
            }
        }
    }
}

