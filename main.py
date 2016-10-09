import csv

def main():

	with open('transaction_data_8451.csv', 'rb') as csvfile:
		spamreader = csv.reader(csvfile, delimiter=' ', quotechar='|')
		for row in spamreader:
			if row[0] == "232" :
				print (', '.join(row))
	
main()
