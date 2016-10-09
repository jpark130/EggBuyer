import csv


def parser():
	with open('transaction_data_8451.csv', 'rb') as csvfile:
		d = {}
		spamreader = csv.reader(csvfile, delimiter=',', quotechar='|')
		for row in spamreader:
			if row[0] == "household_key":
				continue
			if row[0] in d:
				d[row[0]].append(row[1:len(row)])
			else:
				d[row[0]] = [row[1:len(row)]]
		return d

