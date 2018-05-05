import re
string_output = ""
array_output = ""
with open("default_question", "r") as f:
	for line in f:
		line = line.strip().rstrip('\r\n')
		u = line.replace(" ", "_").replace("?", "").replace("-", "_").replace(",", "_").replace("/", "_")
		s = '<string name="{}">{}</string>'.format(u, line)
		a = 'add(c.getString(R.string.{}));'.format(u)
		string_output += s + '\n'
		array_output += a + '\n'

with open("formatted_question", "w") as f:
	f.write(string_output)
	f.write(array_output)