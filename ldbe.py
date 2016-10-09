import parser

def ldbe(m, house_hold, current_day):
	a = m[house_hold]
	for i in range(1, len(a)):
		transaction = a[-i]
		if transaction[16] == "EGGS":
			day = transaction[1]
			return current_day - int(day)
		


