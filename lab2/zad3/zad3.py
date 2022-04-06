def mymax(iterable, key = lambda x: x):
	max_x = max_key = None

	for x in iterable:
		if max_key is None:
			max_x = x
			max_key = key(x)
		else:
			current_key = key(x)
			if current_key > max_key:
				max_x = x
				max_key = current_key

	return max_x

maxint = mymax([1, 3, 5, 7, 4, 6, 9, 2, 0])
print(maxint)

maxchar = mymax("Suncana strana ulice")
print(maxchar)

maxstring = mymax([
	"Gle", "malu", "vocku", "poslije", "kise",
	"Puna", "je", "kapi", "pa", "ih", "njise"])
print(maxstring)

D = {'burek': 8, 'buhtla': 5, 'pletenica': 5}
maxproduct = mymax(D, D.get)
print(maxproduct)

people = [
	('Natasa', 'Maric'),
	('Veljko', 'Horvat'),
	('Ante', 'Filipic'),
	('Ivana', 'Jozic'),
]
maxperson = mymax(people)
print(maxperson)
