Description :
Un transfert consiste en un déplacement d'argent d'un compte émetteur vers un compte bénéficiaire...

Besoin métier :
Intégrer un nouveau cas d'utilisation appelé "Deposit" (Dépôt). Le Deposit représente un dépôt d'argent liquide sur un compte donné (versement d'argent).

Envisagez la situation où vous vous rendez dans une agence avec un montant de 1000DH et que vous effectuez un transfert en spécifiant le RIB souhaité.

L'identifiant fonctionnel d'un compte dans ce cas précis est le RIB.

Assignation :
Le code présente des anomalies de qualité (bonnes pratiques, abstraction, lisibilité...) ainsi que des bugs.

1. Localisez les problèmes au maximum.
2. Tentez d'améliorer la qualité du code.
3. Essayez de résoudre les bugs détectés.
4. Implémentez le cas d'utilisation Deposit.

Règles de gestion :
- Le montant maximal que je peux déposer par opération est de 10 000 DH.
- Ajoutez des tests unitaires.

Nice to have :
- Intégrez une couche de sécurité.
  Usage Instructions:

To build the project, ensure you have the following prerequisites:

- Java 17
- Maven

Build Command:

```bash
mvn clean install
```

Run Command:

```bash
./mvnw spring-boot:run 
## or use your preferred method (IDE, java -jar, docker...)
```

Submission Guidelines:

1. Fork the project to your personal GitLab space.
2. Make the necessary changes.
3. Share the link with us.