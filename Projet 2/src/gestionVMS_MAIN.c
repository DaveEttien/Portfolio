#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT 12345

// Variable globale pour arrêter le serveur
volatile int stop_server = 0;

// Prototype
void* ServerTrans(void *arg);

int main() {
    int server_fd;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addrlen = sizeof(client_addr);

    server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd < 0) { perror("socket"); exit(1); }

    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(PORT);

    if (bind(server_fd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("bind"); exit(1);
    }
    if (listen(server_fd, 5) < 0) { perror("listen"); exit(1); }

    printf("Server ready, waiting for connection...\n");

    while (!stop_server) {
        int client_fd = accept(server_fd, (struct sockaddr*)&client_addr, &addrlen);
        if (client_fd < 0) {
            if (stop_server) break;
            perror("accept");
            continue;
        }
        printf("Client connected!\n");

        int *fd_ptr = malloc(sizeof(int));
        *fd_ptr = client_fd;

        pthread_t tid;
        pthread_create(&tid, NULL, ServerTrans, fd_ptr);
        pthread_detach(tid);
    }

    close(server_fd);
    printf("Serveur arrêté.\n");
    return 0;
}