/* Safe print of two bytes (no unaligned access) */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <pthread.h>
#include <semaphore.h>
#include <signal.h>
#include <unistd.h>
#include "gestionVMS.h"
#include "gestionListeChaineeVMS.h"

#include <semaphore.h>
sem_t semH;
sem_t semQ;
sem_t semC;
sem_t semnbThreadAELX;
sem_t semnbVM;

int nbThreadAELX = 0;
int nbVM = 0;

struct noeudVM *head = NULL;
struct noeudVM *queue = NULL;



void print_two_bytes(const unsigned char *mem) {
    unsigned int i = 1;
    char *c = (char *) &i;
    if (*c == 1) {
        printf("Little Endian\n");
    } else {
        printf("Big Endian\n");
    }

    unsigned short value;

    memcpy(&value, mem, sizeof(value));

    unsigned short m0 = mem[0];
    unsigned short m1 = mem[1];
    unsigned short value_le = m0 | (m1 << 8);

    printf("liitle end %04X\n", value_le);
    printf("liitle end mem[0] %04X  mem[1] %04X \n", mem[0], mem[1]);
    printf("mem original %04X\n", value);
    printf("is little %s\n", value == value_le ? "True" : "False");
    printf("is little Endian *******************%d\n", value == value_le);
}

int read_image_file(uint16_t * memory, char* image_path, uint16_t * origin){
    if (!image_path || !memory || !origin) return 0;

    char fich[200];

    size_t len = strlen(image_path);
    if (len >= sizeof(fich)) len = sizeof(fich) - 1;
    if (len > 0 && image_path[len - 1] == '\n') --len;
    memcpy(fich, image_path, len);
    fich[len] = '\0';

    FILE* file = fopen(fich, "rb");
    if (!file) {
        perror("Cannot open file");
        return 0;
    }


    if (fseek(file, 0, SEEK_END) != 0) {
        perror("fseek failed");
        fclose(file);
        return 0;
    }
    long end_pos = ftell(file);
    if (end_pos < 0) {
        perror("ftell failed");
        fclose(file);
        return 0;
    }
  
    if (fseek(file, 0, SEEK_SET) != 0) {
        perror("fseek failed");
        fclose(file);
        return 0;
    }

 
    if (end_pos < (long)sizeof(uint16_t)) {
        fprintf(stderr, "Image file too small to contain origin\n");
        fclose(file);
        return 0;
    }


    uint16_t file_origin = 0;
    size_t r = fread(&file_origin, sizeof(file_origin), 1, file);
    if (r != 1) {
        fprintf(stderr, "Failed to read origin from image\n");
        fclose(file);
        return 0;
    }

    *origin = file_origin;
    printf("\n\nThe image will be placed at the address 0x%04X\n", *origin);

  
    long bytes_remaining = end_pos - (long)sizeof(uint16_t);
    size_t count = bytes_remaining / sizeof(uint16_t); /* floor */

    if (count == 0) {
   
        fclose(file);
        return 1;
    }


    uint16_t *p = memory + *origin;
    size_t read = fread(p, sizeof(uint16_t), count, file);
    if (read != count) {
        fprintf(stderr, "Warning: only read %zu out of %zu values\n", read, count);
    }


    for (size_t i = 0; i < read; ++i) {

         p[i] = swap16(p[i]); 
    }

    fclose(file);
    return 1;
}
void* ServerTrans(void *arg)
{
    int client_fd = *(int*)arg;
    free(arg);

    char buffer[512];
    printf("ServerTrans lancé avec client_fd = %d\n", client_fd);

    /* compteur global pour numéroter les VMs */
    static int compteurVM = 1;

    while (1) {
        memset(buffer, 0, sizeof(buffer));
        ssize_t n = read(client_fd, buffer, sizeof(buffer) - 1);
        if (n <= 0) {
            printf("Client déconnecté ou erreur lecture.\n");
            break;
        }

        buffer[n] = '\0';
        printf("Transaction reçue : %s\n", buffer);

        char *saveptr = NULL;
  
        char *token = strtok_r(buffer, " \n", &saveptr);

        while (token != NULL) {

            /* --- Commande A : ajouter une VM --- */
            if (strcmp(token, "A") == 0) {
                struct paramE *p = malloc(sizeof(struct paramE));
                if (p) {
                    p->noVM = compteurVM++;
                    pthread_t tid;
                    pthread_create(&tid, NULL, addItem, p);
                    pthread_detach(tid);

                    write(client_fd, "VM ajoutée\n", 11);

                }
            }

            /* --- Commande L : lister des VMs --- */
            else if (strcmp(token, "L") == 0) {
                char *range = strtok_r(NULL, " \n", &saveptr);
                int start, end;
                if (range && sscanf(range, "%d-%d", &start, &end) == 2) {
                    struct paramL *p = malloc(sizeof(struct paramL));
                    if (p) {
                        p->nstart = start;
                        p->nend = end;
                        pthread_t tid;
                        pthread_create(&tid, NULL, listItems, p);
                        pthread_detach(tid);
                        write(client_fd, "Liste envoyée\n", 14);
                    }
                } else {
                    write(client_fd, "Erreur syntaxe L\n", 17);
                }
            }

            /* --- Commande E : exécuter une VM avec fichier par défaut --- */
            else if (strcmp(token, "E") == 0) {
                char *num = strtok_r(NULL, " \n", &saveptr);
                if (num) {
                    struct paramX *p = malloc(sizeof(struct paramX));
                    if (p) {
                        p->noVM = atoi(num);
                        strcpy(p->nomfich, "default.olc3");
                        pthread_t tid;
                        pthread_create(&tid, NULL, executeFile, p);
                        pthread_detach(tid);
                        write(client_fd, "Exécution lancée\n", 17);
                    }
                } else {
                    write(client_fd, "Erreur syntaxe E\n", 17);
                }
            }

            /* --- Commande X : exécuter une VM avec fichier donné --- */
           else if (strcmp(token, "X") == 0) {
                char *num_tok = strtok_r(NULL, " \n", &saveptr);
                char *file_tok = strtok_r(NULL, " \n", &saveptr);

                /* 1️⃣ Vérification AVANT affichage */
                if (!num_tok || !file_tok) {
                    write(client_fd, "Erreur syntaxe X\n", 17);
                    continue;
                }

                /* 2️⃣ Copies stables */
                int noVM = atoi(num_tok);
                char filename[256];
                strncpy(filename, file_tok, sizeof(filename) - 1);
                filename[sizeof(filename) - 1] = '\0';

                /* 3️⃣ Affichage SERVEUR (comme la capture) */
                printf("Received: %s\n", filename);
                printf("Processing : X %d %s\n", noVM, filename);
                fflush(stdout);

                /* 4️⃣ Lancement de l’exécution */
                struct paramX *p = malloc(sizeof(struct paramX));
                if (p) {
                    p->noVM = noVM;
                    strncpy(p->nomfich, filename, sizeof(p->nomfich) - 1);
                    p->nomfich[sizeof(p->nomfich) - 1] = '\0';

                    pthread_t tid;
                    pthread_create(&tid, NULL, executeFile, p);
                    pthread_detach(tid);

                    write(client_fd, "Exécution lancée\n", 17);
            }
        }


            /* --- Commande Q : quitter --- */
            else if (strcmp(token, "q") == 0 || strcmp(token, "Q") == 0) {
                write(client_fd, "Déconnexion...\n", 15);
                close(client_fd);
                pthread_exit(NULL);
            }

            /* --- Commande inconnue --- */
            else {
                char msg[64];
                snprintf(msg, sizeof(msg), "Commande inconnue: %s\n", token);
                write(client_fd, msg, strlen(msg));
            }


            token = strtok_r(NULL, " \n", &saveptr);
        }
    }

    close(client_fd);
    pthread_exit(NULL);
    return NULL;
}

/* executeFile: petit nettoyage - on vérifie ptr != NULL et protège semaphores */


void* executeFile(void* arg) {
    printf("DEBUG: executeFile démarré\n");
    
    struct paramX *param = (struct paramX *)arg;
    if (!param) pthread_exit(NULL);

    char sourcefname[100];
    int noVM = param->noVM;
    strncpy(sourcefname, param->nomfich, sizeof(sourcefname)-1);
    sourcefname[sizeof(sourcefname)-1] = '\0';
    free(param);

    sem_wait(&semnbThreadAELX);
    nbThreadAELX++;
    sem_post(&semnbThreadAELX);

    uint16_t *memory;
    uint16_t origin;
    uint16_t PC_START;
    uint16_t reg[R_COUNT];

    struct noeudVM *ptr = findItem(noVM);
    if(ptr == NULL) {
        sem_wait(&semC);
        printf("Virtual Machine unavailable\n");
        sem_post(&semC);
        sem_wait(&semnbThreadAELX);
        nbThreadAELX--;
        sem_post(&semnbThreadAELX);
        pthread_exit(NULL);
        return NULL;
    }
    memory = ptr->VM.ptrDebutVM;

    if (!read_image_file(memory, sourcefname, &origin)) {
        sem_wait(&semC);
        printf("Failed to load image: %s\n", sourcefname);
        sem_post(&semC);
        
        pthread_exit(NULL);
        return NULL;
    }

 
    while(ptr->VM.busy != 0) { usleep(1000); }
    ptr->VM.busy = 1;

    signal(SIGINT, handle_interrupt);
    disable_input_buffering();

    reg[R_COND] = FL_ZRO;
    PC_START = origin;
    reg[R_PC] = PC_START;

    sem_wait(&semC);

    printf("=== Début exécution LC-3 VM ===\n");
    reg[R_PC] = origin;
    int running = 1;
    while (running) {
    uint16_t instr = memory[reg[R_PC]];
    // Affiche à chaque tour !
    printf("PC: 0x%04X | Instruction: 0x%04X | OP: 0x%X\n", reg[R_PC], instr, instr >> 12);

    uint16_t op = instr >> 12;
    reg[R_PC]++;
    switch(op) {
    case OP_BR:   printf("Op: BR\n"); break;
    case OP_ADD:  printf("Op: ADD\n"); break;
    case OP_LD:   printf("Op: LD\n"); break;
    case OP_ST:   printf("Op: ST\n"); break;
    case OP_JSR:  printf("Op: JSR\n"); break;
    case OP_AND:  printf("Op: AND\n"); break;
    case OP_LDR:  printf("Op: LDR\n"); break;
    case OP_STR:  printf("Op: STR\n"); break;
    case OP_RTI:  printf("Op: RTI\n"); break;
    case OP_NOT:  printf("Op: NOT\n"); break;
    case OP_LDI:  printf("Op: LDI\n"); break;
    case OP_STI:  printf("Op: STI\n"); break;
    case OP_JMP:  printf("Op: JMP\n"); break;
    case OP_RES:  printf("Op: RES (unused)\n"); break;
    case OP_LEA:  printf("Op: LEA\n"); break;
    case OP_TRAP: printf("Op: TRAP\n"); running = 0; break;
    default:      printf("OP inconnu (%d)\n", op); running=0; break;
        }
    }
    
    printf("Fin exécution LC-3\n");

    ptr->VM.busy = 0;
    restore_input_buffering();
    sem_post(&ptr->semNoeud);
    sem_post(&semC);

    sem_wait(&semnbThreadAELX);
    nbThreadAELX--;
    sem_post(&semnbThreadAELX);

    pthread_exit(NULL);
    return NULL;
}
uint16_t swap16(uint16_t x)
{
    return (x << 8) | (x >> 8);
}
struct termios original_tio;

void disable_input_buffering()
{
    tcgetattr(STDIN_FILENO, &original_tio);
    struct termios new_tio = original_tio;
    new_tio.c_lflag &= ~ICANON & ~ECHO;
    tcsetattr(STDIN_FILENO, TCSANOW, &new_tio);
}

void restore_input_buffering()
{
    tcsetattr(STDIN_FILENO, TCSANOW, &original_tio);
}


void handle_interrupt(int signal)
{
    restore_input_buffering();
    printf("\n");
    exit(-2);
}

uint16_t mem_read(uint16_t * memory, uint16_t address)
{
    if (address == MR_KBSR)
    {
        if (check_key())
        {
            memory[MR_KBSR] = (1 << 15);
            memory[MR_KBDR] = getchar();
        }
        else
        {
            memory[MR_KBSR] = 0;
        }
    }
    return memory[address];
}

uint16_t check_key()
{
    fd_set readfds;
    FD_ZERO(&readfds);
    FD_SET(STDIN_FILENO, &readfds);

    struct timeval timeout;
    timeout.tv_sec = 0;
    timeout.tv_usec = 0;
    return select(1, &readfds, NULL, NULL, &timeout) != 0;
}
