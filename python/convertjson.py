import json


class ConvertJSON:
    def __init__(self):
        with open('cbcol_mapping.json', 'r') as cbcol_map:
            self.cbcol_data = json.load(cbcol_map)

        with open('codebook_mapping.json', 'r') as codebook_map:
            self.codebooks_map = json.load(codebook_map)

    #get CBCOL on position - input number X
    def get_cbcol_on_position(self, order):
        return list(self.cbcol_data["CBCOL"][0].keys())[order]

    #get number of all CBCOL in JSON file
    def get_number_of_cbcol(self):
        return len(self.cbcol_data["CBCOL"][0])

    #get list of all CBCOL in json file
    def get_all_cbcol(self):
        return list(self.cbcol_data["CBCOL"][0].keys())

    #get position of cbcol in json file - input name of cbcol such as CBCOL_OrganizationUnit
    def get_cbcol_position(self, codebook):
        position = ConvertJSON().get_all_cbcol().index(codebook)
        return position

    #get name of CBCOL from CSV file - input CB_BankCode (its a self.filename from ReadCSV class)
    def get_cbcol_name(self, codebook):
        return self.codebooks_map[codebook]

    def get_json_value(self, cbcol, value):
        return self.cbcol_data['CBCOL'][0][cbcol][0][value]



#print(ConvertJSON().get_json_value("CBCOL_OrganizationUnit", "displayName [CZ]"))
#print(ConvertJSON().get_cbcol_name('CB_BankCode.csv'))
#print(ConvertJSON().get_codebook_position("CBCOL_CadastreCity"))
#['CBCOL_BankCode', 'CBCOL_OrganizationUnit', 'CBCOL_CadastreCity']
'''
for number in range(ConvertJSON().get_number_of_codebooks()):
   codebook = ConvertJSON().get_codebook(number)
   print(ConvertJSON().get_displayName_CZ(codebook))

#print(ConvertJSON().get_sortOrder('CBCOL_BankCode'))
'''