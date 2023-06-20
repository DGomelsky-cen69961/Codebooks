def replace_ltgt(input_file, output_file):
    with open(input_file, 'r') as f:
        xml_string = f.read()
    # Replace "&lt;" with "<"
    modified_xml = xml_string.replace('&lt;', '<').replace('&gt;', '>')
    # Open the output file for writing
    with open(output_file, 'w') as f:
        # Write the modified XML to the output file
        f.write(modified_xml)

replace_ltgt("FILE_NAME.xml", "FINAL_NAME.xml")