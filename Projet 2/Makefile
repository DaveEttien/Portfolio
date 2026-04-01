all: gestionVMS_MAIN clientTrans

gestionVMS_MAIN: gestionVMS_MAIN.o gestionListeChaineeVMS.o gestionVMS.o
	 gcc -o gestionVMS_MAIN gestionVMS_MAIN.o gestionListeChaineeVMS.o gestionVMS.o

gestionVMS_MAIN.o: gestionVMS_MAIN.c gestionListeChaineeVMS.h gestionVMS.h
	 gcc -c -pthread gestionVMS_MAIN.c -Wall -I.

gestionListeChaineeVMS.o: gestionListeChaineeVMS.c gestionListeChaineeVMS.h gestionVMS.h
	 gcc -c -pthread gestionListeChaineeVMS.c -Wall -I.

gestionVMS.o: gestionVMS.c gestionListeChaineeVMS.h gestionVMS.h
	 gcc -c -pthread gestionVMS.c -Wall -I.

clientTrans: clientTrans.o
	 gcc -o clientTrans clientTrans.o

clientTrans.o: clientTrans.c
	 gcc -c -pthread clientTrans.c -Wall -I.

clean:
	 rm -f gestionVMS_MAIN clientTrans *.o