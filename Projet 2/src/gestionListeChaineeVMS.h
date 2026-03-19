#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <semaphore.h>
#include <pthread.h>
#include <unistd.h>

struct infoVM{						
	int		noVM;
	unsigned char 	busy; 
	unsigned short * 	ptrDebutVM;							
	};								 

struct noeudVM{			
	struct infoVM	VM;		
	struct noeudVM		*suivant;	

	sem_t semNoeud;
	};	


struct paramX{
	int noVM;
	char nomfich[100];
	};
	

struct paramE{
	int noVM;
	};


struct paramL{
	int nstart;
	int nend;
	};



void cls(void);
void error(const int exitcode, const char * message);

struct noeudVM * findItem(const int no);
struct noeudVM * findPrev(const int no);





void* addItem(void * param);





void* removeItem(void* arg);




void* listItems(void* arg);



void saveItems(const char* sourcefname);


void* executeFile(void* arg);


void* readTrans(char* nomFichier);
