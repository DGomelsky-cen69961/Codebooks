import csv
import xml.etree.ElementTree as ET
import xml.dom.minidom as minidom
from readcsv import ReadCSV
from convertjson import ConvertJSON


#"CB_BankCode.csv"
def create_xml_file(file_name, codebook):
    CBCOL = ConvertJSON().get_cbcol_name(codebook)
    Number_of_lines = ReadCSV(codebook).number_of_lines()
    # Create the root element
    root = ET.Element("set")

    # Create the first row
    for csv_row in range(Number_of_lines):
        row1 = ET.SubElement(root, "cz.csas.colman.model.codetablemap.CodeTableMap")
        #row1.set("","")
        ET.SubElement(row1, "searchMode").text = "StartsWith"
        ET.SubElement(row1, "state").text = "<![CDATA[CREATED]]>"
        ET.SubElement(row1, "id").text = "<![CDATA[cscollateral/" + CBCOL + "/" + insert_to_xml(codebook, "referenceName")[csv_row] + "/mapping/CDM/" + insert_to_xml(codebook, "referenceName")[csv_row] + "]]>"
        ET.SubElement(row1, "codeTableCode").text = "<![CDATA[" + CBCOL + "]]>"
        ET.SubElement(row1, "codeTableItemName").text = "<![CDATA[" + insert_to_xml(codebook, "referenceName")[csv_row] + "]]>"
        ET.SubElement(row1, "isPrimaryIn").text = "true"
        ET.SubElement(row1, "isPrimaryOut").text = "true"
        ET.SubElement(row1, "mappedValue").text = "<![CDATA[" + insert_to_xml(codebook, "CDM: mappedValue")[csv_row] + "]] >"
        #ET.SubElement(row1, "mappedValue").text = "<![CDATA[" + Data_mappedValue[csv_row] + "]] >"
        ET.SubElement(row1, "system").text = "<![CDATA[CDM]] >"

     # Create the XML tree
    tree = ET.ElementTree(root)

     # Write the tree to a file
    xml_string = ET.tostring(root, encoding="utf-8")
    xml_pretty_string = minidom.parseString(xml_string).toprettyxml(indent=" ")
    with open(file_name, "w") as file:
        file.write(xml_pretty_string)

    #return data of CSV need CBCOL and
def insert_to_xml(codebook, value):
    CSV_Codebook = ReadCSV(codebook)
    Number_of_lines = CSV_Codebook.number_of_lines()
    CBCOL = ConvertJSON().get_cbcol_name(CSV_Codebook.file_name)
    JSON_Value = ConvertJSON().get_json_value(CBCOL, value)
    if len(JSON_Value) == 0:
        Data_JSON_Value = ["" for _ in range(Number_of_lines)]
    else:
        Position_JSON_value = CSV_Codebook.get_header_position(JSON_Value)
        Data_JSON_Value = CSV_Codebook.read_column(Position_JSON_value)
    return Data_JSON_Value



create_xml_file("pokusFINAL.xml", "CB_BankCode.csv")
