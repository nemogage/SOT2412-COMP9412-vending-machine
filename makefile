run:
	gradle run

test:
	gradle test

clean:
	gradle clean
	rm app/reports/*
	touch app/reports/.GITpush.sh

clean_run:
	gradle clean
	rm app/reports/*
	touch app/reports/.GITpush.sh
	gradle run

full:
	gradle clean
	rm app/reports/*
	touch app/reports/.GITpush.sh
	gradle test
	gradle run