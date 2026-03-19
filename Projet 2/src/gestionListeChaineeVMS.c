#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <pthread.h>
#include <semaphore.h>
#include "gestionListeChaineeVMS.h"
#include "gestionVMS.h"

#define MAX_MEMORY 65536



extern struct noeudVM* head;
extern struct noeudVM* queue;
extern int nbVM;
extern int nbThreadAELX;

// Sémaphores externes
extern sem_t semH, semQ, semnbVM, semC, semnbThreadAELX;

// Recherche un item dans la liste chaînée
struct noeudVM * findItem(const int no){
    sem_wait(&semH);
    sem_wait(&semQ);

    if ((head == NULL) && (queue == NULL)) {
        sem_post(&semQ);
        sem_post(&semH);
        return NULL;
    }

    struct noeudVM* ptr = head;
    if(ptr == NULL){
        sem_post(&semQ);
        sem_post(&semH);
        return NULL;
    }
    sem_wait(&ptr->semNoeud);
    sem_post(&semQ);
    sem_post(&semH);

    // Premier noeud
    if(ptr->VM.noVM == no)
        return ptr;

    while(ptr->suivant != NULL){
        if(ptr->suivant == NULL){
            sem_post(&(ptr->semNoeud));
            return NULL;
        }
        sem_wait(&(ptr->suivant->semNoeud));
        struct noeudVM* optr = ptr;
        ptr = ptr->suivant;
        sem_post(&(optr->semNoeud));
        if(ptr->VM.noVM == no)
            return ptr;
    }

    // Aucun trouvé : relâcher le dernier lock
    sem_post(&(ptr->semNoeud));
    return NULL;
}



// Ajoute un item à la fin de la liste chaînée de VM
void* addItem(void * arg){
    sem_wait(&semnbThreadAELX);
    nbThreadAELX++;
    sem_post(&semnbThreadAELX);

    struct noeudVM* ni = malloc(sizeof(struct noeudVM));
    if (!ni) {
        fprintf(stderr, "Erreur malloc noeudVM !\n");
        exit(2);
    }
    ni->VM.noVM = ++nbVM;
    ni->VM.busy = 0;
    ni->VM.ptrDebutVM = malloc(sizeof(uint16_t)*MAX_MEMORY);
    if (!ni->VM.ptrDebutVM) {
        fprintf(stderr, "Erreur malloc mémoire VM !\n");
        exit(3);
    }
    printf("VM #%d créée : %p\n", ni->VM.noVM, ni->VM.ptrDebutVM);

    sem_init(&(ni->semNoeud),0,1);

    sem_wait(&semH);
    sem_wait(&semQ);

    if ((head == NULL) && (queue == NULL)) {
        ni->suivant = NULL;
        queue = head = ni;
        sem_post(&semQ);
        sem_post(&semH);
        sem_wait(&semnbThreadAELX);
        nbThreadAELX--;
        sem_post(&semnbThreadAELX);
        pthread_exit(0);
        return NULL;
    }

    if(queue != NULL)
        sem_wait(&(queue->semNoeud));
    sem_post(&semH);

    struct noeudVM* tptr = queue;
    ni->suivant = NULL;
    queue = ni;
    if (tptr) tptr->suivant = ni;
    if (tptr) sem_post(&(tptr->semNoeud));

    sem_post(&semQ);
    sem_wait(&semnbThreadAELX);
    nbThreadAELX--;
    sem_post(&semnbThreadAELX);
    pthread_exit(NULL);
    return NULL;
}

void* removeItem(void* arg){
    struct paramE *param = (struct paramE *)arg;
    int noVM = param->noVM;
    free(param);

    while(1) {
        sem_wait(&semnbThreadAELX);
        if(nbThreadAELX == 0) break;
        sem_post(&semnbThreadAELX);
    }

    if ((noVM < 1) || ((head == NULL) && (queue == NULL))) {
        sem_post(&semnbThreadAELX);
        pthread_exit(0);
        return NULL;
    }

    struct noeudVM *ptr = (noVM == 1) ? head : findItem(noVM-1);

    if(ptr != NULL){
        sem_post(&(ptr->semNoeud));
        nbVM--;

        if((head == ptr) && (noVM == 1)){
            if(head==queue){
                sem_destroy(&(ptr->semNoeud));
                free(ptr->VM.ptrDebutVM);
                free(ptr);
                queue = head = NULL;
                sem_post(&semnbThreadAELX);
                pthread_exit(0);
            }
            struct noeudVM *tptr = ptr->suivant;
            head = tptr;
            sem_destroy(&(ptr->semNoeud));
            free(ptr->VM.ptrDebutVM);
            free(ptr);
        }
        else if (queue == ptr->suivant){
            queue = ptr;
            sem_destroy(&(ptr->suivant->semNoeud));
            free(ptr->suivant->VM.ptrDebutVM);
            free(ptr->suivant);
            ptr->suivant = NULL;
            sem_post(&semnbThreadAELX);
            pthread_exit(0);
        }
        else{
            struct noeudVM *optr = ptr->suivant;
            ptr->suivant = ptr->suivant->suivant;
            struct noeudVM *tptr = ptr->suivant;
            sem_destroy(&(optr->semNoeud));
            free(optr->VM.ptrDebutVM);
            free(optr);
            while (tptr != NULL){
                tptr->VM.noVM--;
                tptr = tptr->suivant;
            }
        }
        sem_post(&semnbThreadAELX);
    } else {
        sem_post(&semnbThreadAELX);
    }
    pthread_exit(NULL);
    return NULL;
}

void* listItems(void* arg){
    struct paramL *param = (struct paramL *)arg;
    int start = param->nstart;
    int end = param->nend;
    free(param);

    sem_wait(&semnbThreadAELX);
    nbThreadAELX++;
    sem_post(&semnbThreadAELX);

    sem_wait(&semC); // Exclusion console
    sem_wait(&semH);

    printf("noVM  Busy?   Adresse Debut VM\n");
    printf("=============================================\n");

    if (head == NULL) {
        sem_post(&semH);
        sem_post(&semC);
        sem_wait(&semnbThreadAELX);
        nbThreadAELX--;
        sem_post(&semnbThreadAELX);
        pthread_exit(NULL);
        return NULL;
    }
    struct noeudVM *ptr = head;
    sem_wait(&(ptr->semNoeud));
    sem_post(&semH);

    while (ptr != NULL) {
        if ((ptr->VM.noVM >= start) && (ptr->VM.noVM <= end)) {
            printf("%d \t %d \t %p\n", ptr->VM.noVM, ptr->VM.busy, ptr->VM.ptrDebutVM);
        }
        if (ptr->VM.noVM > end) {
            struct noeudVM* optr = ptr;
            ptr = NULL;
            sem_post(&(optr->semNoeud));
        } else {
            struct noeudVM* optr = ptr;
            if (ptr->suivant != NULL) sem_wait(&(ptr->suivant->semNoeud));
            ptr = ptr->suivant;
            sem_post(&(optr->semNoeud));
        }
    }
    printf("=============================================\n\n");
    sem_post(&semC);
    sem_wait(&semnbThreadAELX);
    nbThreadAELX--;
    sem_post(&semnbThreadAELX);
    pthread_exit(NULL);
    return NULL;
}

 void* executeFile(void* arg);
