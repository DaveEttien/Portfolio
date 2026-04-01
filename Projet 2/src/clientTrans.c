#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT 12345
#define SERVER_IP "127.0.0.1"

int main()
{
    int sock_fd;
    struct sockaddr_in server_addr;
    char ligne[256];

    sock_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (sock_fd < 0) {
        perror("socket");
        exit(1);
    }

    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    inet_pton(AF_INET, SERVER_IP, &server_addr.sin_addr);

    if (connect(sock_fd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("connect");
        exit(1);
    }

    printf("Client prêt. Entrez vos transactions (A, E , L , X , q):\n");

    while (1) {

        printf("\nTransaction: ");

        if (!fgets(ligne, sizeof(ligne), stdin))
            break;

        /* Envoi de la ligne au serveur */
        write(sock_fd, ligne, strlen(ligne));

        /* Lecture de la réponse */
        char reponse[256];
        ssize_t n = read(sock_fd, reponse, sizeof(reponse) - 1);
        if (n > 0) {
            reponse[n] = '\0';
            printf("Réponse serveur: %s", reponse);
        }
    }

    close(sock_fd);
    return 0;
}
