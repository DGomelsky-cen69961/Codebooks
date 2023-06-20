import csv


class ReadCSV:
    def __init__(self, file_name):
        self.file_name = file_name
        with open(self.file_name, 'r') as file:
            self.reader = csv.reader(file, delimiter=';', quotechar='"')
            self.header = next(self.reader)
        # self.reader = self.read_csv_file()
        # self.header = self.read_csv_header()

    def read_csv_file(self):
        # with open("./CB_BankCode.csv", 'r') as file:
        with open(self.file_name, 'r') as file:
            csvreader = csv.reader(file, delimiter=';', quotechar='"')
        return csvreader

    def read_csv_header(self):
        with open(self.file_name, 'r') as file:
            csvreader = csv.reader(file, delimiter=';', quotechar='"')
            csvheader = next(csvreader)
        return csvheader

    def get_header(self):
        return self.header

    def get_header_position(self, value):
        if value in self.header:
            return self.header.index(value)
        else:
            return None

    # read column on index X - want to use it with get_header_position
    def read_column(self, column_index):
        column_data = []
        with open(self.file_name, 'r') as file:
            reader = csv.reader(file, delimiter=';', quotechar='"')
            next(reader)
            for row in reader:
                column_data.append(row[column_index])
            return column_data

    def number_of_lines(self):
        return len(ReadCSV(self.file_name).read_column(10))

#print(ReadCSV("CB_BankCode.csv").number_of_lines())
#print(ReadCSV("CB_BankCode.csv").get_header_position("ID"))
'''
ReadCSV = ReadCSV("CB_BankCode.csv")

head = ReadCSV.get_header()
print(head)


read = ReadCSV.read_column(ReadCSV.findPosition("ID"))

for value in range(len(read)):
    print(read[value])
#print(ReadCSV().findPosition("ID"))
'''
