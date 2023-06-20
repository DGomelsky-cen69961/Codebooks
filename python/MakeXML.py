import csv
import xml.etree.ElementTree as ET
import xml.dom.minidom as minidom
from readcsv import ReadCSV
from convertjson import ConvertJSON


class MakeXML:
    def __init__(self, codebook):
        self.codebook = codebook

    #First XML document
    def create_CodeTableMapping(self):
        CBCOL = ConvertJSON().get_cbcol_name(self.codebook)
        Number_of_lines = ReadCSV(self.codebook).number_of_lines()
        file_name = "CodeTableMapping_" + CBCOL + "_CDM.xml"
        tmp_file_name = "temp" + file_name

        # Create the root element
        root = ET.Element("set")

        # Create the first row
        for csv_row in range(Number_of_lines):
            row1 = ET.SubElement(root, "cz.csas.colman.model.codetablemap.CodeTableMap")
            #row1.set("","")
            ET.SubElement(row1, "searchMode").text = "StartsWith"
            ET.SubElement(row1, "state").text = "<![CDATA[CREATED]]>" #+ chr(62)
            ET.SubElement(row1, "id").text = "<![CDATA[cscollateral/" + CBCOL + "/" + self.insert_to_xml("referenceName")[csv_row] + "/mapping/CDM/" + self.insert_to_xml("referenceName")[csv_row] + "]]>"
            ET.SubElement(row1, "codeTableCode").text = "<![CDATA[" + CBCOL + "]]>"
            ET.SubElement(row1, "codeTableItemName").text = "<![CDATA[" + self.insert_to_xml("referenceName")[csv_row] + "]]>"
            ET.SubElement(row1, "isPrimaryIn").text = "true"
            ET.SubElement(row1, "isPrimaryOut").text = "true"
            ET.SubElement(row1, "mappedValue").text = "<![CDATA[" + self.insert_to_xml("CDM: mappedValue")[csv_row] + "]]>"
            ET.SubElement(row1, "system").text = "<![CDATA[CDM]]>"

         # Create the XML tree
        tree = ET.ElementTree(root)

         # Write the tree to a file
        xml_string = ET.tostring(root, encoding="utf-8")
        xml_pretty_string = minidom.parseString(xml_string).toprettyxml(indent=" ")
        with open(tmp_file_name, "w", encoding="utf-8") as file:
            file.write(xml_pretty_string)

        self.replace_ltgt(tmp_file_name, file_name)

    #Second XML document
    def create_UniversalLanguage(self):
        CBCOL = ConvertJSON().get_cbcol_name(self.codebook)
        Number_of_lines = ReadCSV(self.codebook).number_of_lines()
        file_name = "UniversalLanguage_" + CBCOL + ".xml"
        tmp_file_name = "temp" + file_name

        # Create the root element
        root = ET.Element("set")

        # Create the first row
        for csv_row in range(Number_of_lines):
            row1 = ET.SubElement(root, "hu.appello.webdp.model.UniversalLanguage")
            # row1.set("","")
            ET.SubElement(row1, "state").text = "CREATED"
            ET.SubElement(row1, "id").text = "<![CDATA[cscollateral/" + CBCOL + "-" + self.insert_to_xml("referenceName")[csv_row] + "]]>"
            ET.SubElement(row1, "hu").text = "<![CDATA[" + self.insert_to_xml("displayName [EN]")[csv_row] + "]]>"
            ET.SubElement(row1, "en").text = "<![CDATA[" + self.insert_to_xml("displayName [EN]")[csv_row] + "]]>"
            ET.SubElement(row1, "cz").text = "<![CDATA[" + self.insert_to_xml("displayName [CZ]")[csv_row] + "]]>"

            row2 = ET.SubElement(row1, "module")
            ET.SubElement(row2, "state").text = "<![CDATA[CREATED]]>"
            ET.SubElement(row2, "id").text = "<![CDATA[cscollateral]]>"
            ET.SubElement(row2, "referenceName").text = "<![CDATA[cscollateral]]>"
            ET.SubElement(row2, "displayNameUniv").text = "<![CDATA[cscollateral/modultitle]]>"

        # Create the XML tree
        tree = ET.ElementTree(root)

        # Write the tree to a file
        xml_string = ET.tostring(root, encoding="utf-8")
        xml_pretty_string = minidom.parseString(xml_string).toprettyxml(indent=" ")
        with open(tmp_file_name, "w", encoding="utf-8") as file:
            file.write(xml_pretty_string)

        self.replace_ltgt(tmp_file_name, file_name)

    #Third XML document
    def create_wdp(self):
        CBCOL = ConvertJSON().get_cbcol_name(self.codebook)
        Number_of_lines = ReadCSV(self.codebook).number_of_lines()
        file_name = "wdp_" + CBCOL + ".xml"
        tmp_file_name = "temp" + file_name

        # Create the root element
        root = ET.Element("hu.appello.webdp.model.CodeBase")
        # Create the first row
        ET.SubElement(root, "searchMode").text = "StartsWith"
        ET.SubElement(root, "state").text = "<![CDATA[CREATED]]>"
        ET.SubElement(root, "id").text = "<![CDATA[cscollateral/" + CBCOL + "]]>"
        ET.SubElement(root, "referenceName").text = "<![CDATA[" + CBCOL + "]]>"
        ET.SubElement(root, "displayNameUniv").text = "<![CDATA[cscollateral/" + CBCOL + "]]>"
        ET.SubElement(root, "fieldType").text = "<![CDATA[StringField]]>"
        row2 = ET.SubElement(root, "module")
        ET.SubElement(row2, "state").text = "<![CDATA[CREATED]]>"
        ET.SubElement(row2, "id").text = "<![CDATA[cscollateral]]>"
        ET.SubElement(row2, "referenceName").text = "<![CDATA[cscollateral]]>"
        ET.SubElement(row2, "displayNameUniv").text = "<![CDATA[cscollateral/modultitle]]>"
        row3 = ET.SubElement(root, "codeElements")
        for csv_row in range(Number_of_lines):
            row4 = ET.SubElement(row3, "hu.appello.webdp.model.CodeElement")
            ET.SubElement(row4, "state").text = "CREATED"
            ET.SubElement(row4, "id").text = "<![CDATA[cscollateral/" + CBCOL + "/" + self.insert_to_xml("referenceName")[csv_row] + "]]>"
            ET.SubElement(row4, "referenceName").text = "<![CDATA[" + self.insert_to_xml("referenceName")[csv_row] + "]]>"
            ET.SubElement(row4, "sortOrder").text = "<![CDATA[" + self.insert_to_xml("sortOrder")[csv_row] + "]]>"
            ET.SubElement(row4, "active").text = self.insert_to_xml_true_false("active")
            ET.SubElement(row4, "displayNameUniv").text = "<![CDATA[cscollateral/" + CBCOL + "-" + self.insert_to_xml("referenceName")[csv_row] + "]]>"
            ET.SubElement(row4, "usable").text = "true"
            ET.SubElement(row4, "codeBase").attrib["reference"] = "../../.." #"<codeBase reference=\"../../..\"/>"
            ET.SubElement(row4, "description").text = "<![CDATA[" + self.insert_to_xml("description [CZ]")[csv_row] + "]]>"

            # Create the XML tree
        tree = ET.ElementTree(root)

        # Write the tree to a file
        xml_string = ET.tostring(root, encoding="utf-8")
        xml_pretty_string = minidom.parseString(xml_string).toprettyxml(indent=" ")
        with open(tmp_file_name, "w", encoding="utf-8") as file:
            file.write(xml_pretty_string)
        
        self.replace_ltgt(tmp_file_name, file_name)

    #return data of CSV value must be from key from current cbcol in file cbcol_mapping.json
    def insert_to_xml(self, value):
        CSV_Codebook = ReadCSV(self.codebook)
        Number_of_lines = CSV_Codebook.number_of_lines()
        CBCOL = ConvertJSON().get_cbcol_name(CSV_Codebook.file_name)
        JSON_Value = ConvertJSON().get_json_value(CBCOL, value)
        if len(JSON_Value) == 0:
            Data_JSON_Value = ["" for _ in range(Number_of_lines)]
        else:
            Position_JSON_value = CSV_Codebook.get_header_position(JSON_Value)
            Data_JSON_Value = CSV_Codebook.read_column(Position_JSON_value)
        return Data_JSON_Value

    def insert_to_xml_true_false(self, value):
        CSV_Codebook = ReadCSV(self.codebook)
        Number_of_lines = CSV_Codebook.number_of_lines()
        CBCOL = ConvertJSON().get_cbcol_name(CSV_Codebook.file_name)
        Boolean_Value = ConvertJSON().get_json_value(CBCOL, value)
        if len(Boolean_Value) == 0:
            Data_Boolean_Value = ["" for _ in range(Number_of_lines)]
        else:
            Position_Boolean_value = CSV_Codebook.get_header_position(Boolean_Value)
            Data_Boolean_Value = CSV_Codebook.read_column(Position_Boolean_value)

        if Data_Boolean_Value == 0:
            return_boolean_value = "false"
        else:
            return_boolean_value = "true"
        return return_boolean_value

    #Replace '&lt;' and '&gt;' in XML file with '<' '>'
    def replace_ltgt(self, input_file, output_file):
        with open(input_file, 'r', encoding="utf8") as f:
            xml_string = f.read()
        # Replace "&lt;" with "<"
        modified_xml = xml_string.replace('&lt;', '<').replace('&gt;', '>')
        # Open the output file for writing
        with open(output_file, 'w') as f:
            # Write the modified XML to the output file
            f.write(modified_xml)

MakeXML("CB_BankCode.csv").create_CodeTableMapping()
#MakeXML("CB_BankCode.csv").create_wdp()


