#include <stdio.h>
#include <stdint.h>

// gcc -c generateOBJ_multiple_instructions.c -Wall -I.
// gcc -o generateOBJ_multiple_instructions generateOBJ_multiple_instructions.o
// ./generateOBJ_multiple_instructions

//#########################################################
//#
//# Titre : 	UTILITAIRES (generateOBJ_multiple_instructions) TP1 LINUX Automne 25
//#				SIF-1015 - Système d'exploitation
//#				Université du Québec à Trois-Rivières
//#            
//# But :  	Écrire dans un fichier binaire
//#			Écrire 0x3000
//#			suivi par l'un ensemble d'instructions binaires 
			 
//#        génère un olc3 (code machine)
//#
//# Auteur : 	.....
//#	Date :		Septembre 2025
//#
//# Langage : 	ANSI C on LINUX 
//#
//#######################################
int main(){

//Partie 1:Fichier binaire A LC-3
	FILE *programmeA = fopen("programmeA.olc3","wb");

	if(!programmeA){
	perror("Erreur d'ouverture du fichier binaire");
	return 1;
	}

	uint16_t adresseA = 0x3000;
	fputc(adresseA >> 8, programmeA);
	fputc(adresseA & 0xFF, programmeA);

	uint16_t instructionsA[] = {
	0x5260,
	0x106A,
	0xF021,
	0xF025
	};

	// Ecrire chaque instruction en big indian
	for(int i =0 ; i<4; i++){
	fputc(instructionsA[i] >> 8, programmeA);
	fputc(instructionsA[i] & 0xFF, programmeA);
	}

	fclose(programmeA);
	printf("Fichier programeA.olc3 généré avec succès.\n");

//Partie 2:Fichier binaire B LC-3
 FILE *programmeB = fopen("programmeB.olc3","wb");

        if(!programmeB){
        perror("Erreur d'ouverture du fichier binaire");
        return 1;
        }

        uint16_t adresseB = 0x3000;
        fputc(adresseB >> 8, programmeB);
        fputc(adresseB & 0xFF, programmeB);

        uint16_t instructionsB[] = {
        0x5260,
        0x1076,
        0xF021,
        0xF025
        };

        // Ecrire chaque instruction en big indian
        for(int i =0 ; i<4; i++){
        fputc(instructionsB[i] >> 8, programmeB);
        fputc(instructionsB[i] & 0xFF, programmeB);
	}
        fclose(programmeB);
        printf("Fichier programeB.olc3 généré avec succès.\n");

	return 0 ;

}